package xyz.wim.dentrealitytask.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import xyz.wim.dentrealitytask.data.Country
import xyz.wim.dentrealitytask.data.CountryRepository

class MapViewModel(private val countryRepo: CountryRepository) : ViewModel() {


    val countries: LiveData<List<Country>> = liveData {
        val data = countryRepo.getCountries()
        emit(data)
    }
}