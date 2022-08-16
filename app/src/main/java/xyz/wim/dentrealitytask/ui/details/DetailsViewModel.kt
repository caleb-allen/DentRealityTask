package xyz.wim.dentrealitytask.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.wim.dentrealitytask.data.Country
import xyz.wim.dentrealitytask.data.CountryRepository

class DetailsViewModel(private val countryRepo: CountryRepository) : ViewModel() {
    val country: MutableLiveData<Country> by lazy {
        MutableLiveData<Country>()
    }

    fun getCountryDetails(countryCode: String) {
        countryRepo.getCountry(countryCode)?.let {
            country.postValue(it)
        }
    }
}