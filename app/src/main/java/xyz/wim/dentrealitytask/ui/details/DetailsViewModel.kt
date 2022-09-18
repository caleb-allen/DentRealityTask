package xyz.wim.dentrealitytask.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.wim.dentrealitytask.data.Country
import xyz.wim.dentrealitytask.data.CountryRepository
import xyz.wim.dentrealitytask.data.HomeManager

class DetailsViewModel(
    private val countryCode: String,
    private val countryRepo: CountryRepository,
    private val homeManager: HomeManager
) : ViewModel() {
    val country: MutableLiveData<Country> = MutableLiveData()

    val homeDetails: MutableLiveData<HomeManager.HomeDetails> = MutableLiveData()

    fun getCountryDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            country.postValue(countryRepo.getCountry(countryCode))
            val homeDetails = homeManager.getHomeDetails(countryCode)
            this@DetailsViewModel.homeDetails.postValue(homeDetails)
        }
    }

    fun setAsHome() {
        country.value?.let {
            homeManager.setHome(it.countryCode)
        }
        getCountryDetails()
    }
}
