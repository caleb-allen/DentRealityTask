package xyz.wim.dentrealitytask.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import xyz.wim.dentrealitytask.R
import xyz.wim.dentrealitytask.data.HomeManager
import xyz.wim.dentrealitytask.databinding.DetailsFragmentBinding
import java.text.NumberFormat

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance(countryCode: String): DetailsFragment {
            val f = DetailsFragment()
            val b = Bundle()
            b.putString(KEY_COUNTRY_CODE, countryCode)
            f.arguments = b
            return f
        }

        private const val KEY_COUNTRY_CODE = "KEY_COUNTRY_CODE"
    }

    private var _binding: DetailsFragmentBinding? = null
    private lateinit var countryCode: String

    private val binding get() = _binding!!

    private val vm: DetailsViewModel by viewModel { parametersOf(countryCode) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        val view = _binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(KEY_COUNTRY_CODE)?.let { countryCode ->
            this.countryCode = countryCode
            vm.getCountryDetails()
        }
        vm.country.observe(viewLifecycleOwner) { country ->
            with(binding) {
                countryCode.text = country.countryCode
                name.text = country.name
                timezones.text = country.timezones.joinToString()
                capital.text = country.capital
            }
        }
        vm.homeDetails.observe(viewLifecycleOwner) { details ->
            displayHomeDetails(details)
        }

        with(binding) {
            btnHome.setOnClickListener {
                vm.setAsHome()
            }
        }
    }

    private fun displayHomeDetails(details: HomeManager.HomeDetails): Any = with(binding) {
        return when (details) {
            HomeManager.HomeDetails.None -> {
                distance.isVisible = false
                distanceLabel.isVisible = false

                btnHome.isVisible = true
                imgHome.isVisible = false
            }
            is HomeManager.HomeDetails.DistanceFromHome -> {
                distance.isVisible = true
                distanceLabel.isVisible = true

                distanceLabel.text = getString(R.string.distance_from_x, details.name)
                val nf: NumberFormat = NumberFormat.getInstance()
                // convert distance from m to km
                val formattedDistance = nf.format(details.distance / 1000)
                distance.text = getString(R.string.x_km, formattedDistance)

                btnHome.isVisible = true
                imgHome.isVisible = false
            }
            HomeManager.HomeDetails.Home -> {
                distanceLabel.isVisible = false
                distance.isVisible = false

                btnHome.isVisible = false
                imgHome.isVisible = true
            }
        }
    }
}
