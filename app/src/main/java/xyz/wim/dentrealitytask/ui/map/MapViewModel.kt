package xyz.wim.dentrealitytask.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.schedulers.IoScheduler
import timber.log.Timber
import xyz.wim.dentrealitytask.data.Country
import xyz.wim.dentrealitytask.data.CountryRepository
import java.util.concurrent.TimeUnit

class MapViewModel(private val countryRepo: CountryRepository) : ViewModel() {


    val countries: MutableLiveData<List<Country>> by lazy {
        MutableLiveData<List<Country>>()
    }

    init {

        countryRepo.getCountries().let {
            Single.just(1)
                // TODO would be better to inject scheduler
                .subscribeOn(IoScheduler())
                // allow UI to settle so that views can be correctly measured
                // (very hacky..)
                .delay(1, TimeUnit.SECONDS)
                .subscribe { _ ->
                    countries.postValue(it)
                    Timber.d("Posting list of countries")
                }
        }
    }
}