package xyz.wim.dentrealitytask.ui.map

import android.R.attr.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import xyz.wim.dentrealitytask.R
import xyz.wim.dentrealitytask.data.Country
import xyz.wim.dentrealitytask.databinding.DetailsFragmentBinding
import xyz.wim.dentrealitytask.databinding.MapFragmentBinding
import xyz.wim.dentrealitytask.ui.details.DetailsFragment
import kotlin.math.roundToInt


class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }

    private var root: View? = null

    private var _binding: MapFragmentBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val vm: MapViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MapFragmentBinding.inflate(inflater, container, false)
        val view = _binding!!.root
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        vm.countries.observe(this) {
            Timber.d("Observing countries")
            createCountryViews(it)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createCountryViews(countries: List<Country>) {
        for (country in countries) {
            createCountryView(country)
        }
    }

    /* private val defaultConstraintSet: ConstraintSet by lazy {
         with(binding) {

             val set = ConstraintSet()
             set.clone()
             set.connect()
             set
         }
     }*/
    private fun createCountryView(country: Country) {
        Timber.d("Creating view for ${country.name}")
        val (pctX, pctY) = MapUtils.coordToPercent(country.latlng.last(), country.latlng.first())

        with(binding) {
            val pixelX = pctX * mapContainer.width
            val pixelY = pctY * mapContainer.height
            Timber.d("Pixel coordinates: \n\tpixelX: $pixelX, pixelY: $pixelY")

            val countryView = layoutInflater.inflate(R.layout.map_view_item, mapContainer, false)
            countryView.id = View.generateViewId()
            countryView.setOnClickListener {
                // TODO this could be extracted to its own class which manages
                //  fragments and navigation, initiated by events on a shared
                //  event bus
                activity?.let {
                    it.supportFragmentManager.beginTransaction()
//                        .
                        .replace(R.id.container, DetailsFragment.newInstance(country.countryCode))
                        .commitNow()
                }
            }

            mapContainer.addView(countryView)

            countryView.updateLayoutParams<FrameLayout.LayoutParams> {
                setMargins(pixelX.roundToInt(), pixelY.roundToInt(), 0, 0)
            }

            mapContainer.invalidate()
        }
    }

}