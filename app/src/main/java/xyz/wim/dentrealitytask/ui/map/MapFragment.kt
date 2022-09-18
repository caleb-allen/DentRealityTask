package xyz.wim.dentrealitytask.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import xyz.wim.dentrealitytask.R
import xyz.wim.dentrealitytask.await
import xyz.wim.dentrealitytask.databinding.MapFragmentBinding
import xyz.wim.dentrealitytask.ui.details.DetailsFragment


class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()

        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    private var _binding: MapFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val vm: MapViewModel by viewModel()

    private val mapChannel = Channel<GoogleMap>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapFragmentBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                displayCountryMarkers()
            }
        }
        with(binding) {
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync {
                lifecycleScope.launch {
                    mapChannel.send(it)
                }
            }
        }

        return _binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        vm.countries.observe(this) {
            Timber.d("Observing countries")
        }
        super.onCreate(savedInstanceState)
    }

    private suspend fun displayCountryMarkers() {
        val map = mapChannel.receive()
        val countries = vm.countries.await()

        for (country in countries) {
            val latlng = LatLng(country.latlng[0], country.latlng[1])
            map.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title(country.countryCode)
            )
        }
        map.setOnMarkerClickListener { marker ->
            Timber.d("Marker clicked: %s", marker.title)
            activity?.let { activity ->
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(marker.title!!))
                    .addToBackStack(null)
                    .commit()
            }
            return@setOnMarkerClickListener true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)

        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        _binding?.mapView?.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}
