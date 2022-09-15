package xyz.wim.dentrealitytask.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import xyz.wim.dentrealitytask.data.Country
import xyz.wim.dentrealitytask.data.CountryRepository
import kotlin.coroutines.coroutineContext

class DetailsViewModel(private val countryRepo: CountryRepository) : ViewModel() {
    private val selectedCountry = Channel<Country>()
    val country : LiveData<Country> = liveData {
        while(coroutineContext.isActive){
            val c = selectedCountry.receive()
            emit(c)
            yield()
        }
    }

    fun getCountryDetails(countryCode: String) {
        viewModelScope.launch {
            countryRepo.getCountry(countryCode)?.let {
                selectedCountry.send(it)
            } ?: kotlin.run {
                error("Country details null. Attempted to get country with code %s"
                    .format(countryCode))
            }
        }
    }

    fun toggleFavorite() {
        // TODO
    }
}