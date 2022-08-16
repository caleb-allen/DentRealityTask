package xyz.wim.dentrealitytask.data

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import timber.log.Timber
import xyz.wim.dentrealitytask.R

@ExperimentalStdlibApi
class LocalCountryRepository(private val context: Context,
                             private val moshi: Moshi): CountryRepository {
    override fun getCountries(): List<Country> {
        val json = context.resources.openRawResource(R.raw.countries).bufferedReader().use { it.readText() }
        val adapter = moshi.adapter<List<Country>>()
//        Timber.d(json)
        val countries = adapter.fromJson(json)!!
        return countries
    }

    override fun getCountry(countryCode: String): Country? =
        getCountries().firstOrNull { it.countryCode == countryCode }

}