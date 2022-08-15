package xyz.wim.dentrealitytask.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import xyz.wim.dentrealitytask.data.Country
import xyz.wim.dentrealitytask.data.CountryRepository

class MapViewModel(private val countryRepo: CountryRepository) : ViewModel() {


    // Create a LiveData with a String
    val countries: MutableLiveData<List<Country>> by lazy {
        MutableLiveData<List<Country>>()
    }
    init {

        countryRepo.getCountries().let {
            Timber.d("Posting list of countries")
            countries.postValue(it)
        }
    }
}