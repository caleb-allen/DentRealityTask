package xyz.wim.dentrealitytask.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.wim.dentrealitytask.R
import xyz.wim.dentrealitytask.databinding.DetailsFragmentBinding

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

    private val binding get() = _binding!!

    private val vm: DetailsViewModel by viewModel()

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
            vm.getCountryDetails(countryCode)
        }

        vm.country.observe(viewLifecycleOwner) { country ->
            with(binding) {
                countryCode.text = country.countryCode
                name.text = country.name
                timezones.text = country.timezones.joinToString()
                capital.text = country.capital
            }
        }
    }
}