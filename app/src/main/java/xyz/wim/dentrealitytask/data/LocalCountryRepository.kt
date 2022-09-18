package xyz.wim.dentrealitytask.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import xyz.wim.dentrealitytask.R

@ExperimentalStdlibApi
class LocalCountryRepository(private val context: Context,
                             private val moshi: Moshi): CountryRepository {
    override suspend fun getCountries(): List<Country> {
        val json = context.resources.openRawResource(R.raw.countries).bufferedReader().use { it.readText() }
        val adapter = moshi.adapter<List<Country>>()
        val countries = adapter.fromJson(json)!!
        return countries
    }

    override suspend fun getCountry(countryCode: String): Country =
        getCountries().first { it.countryCode == countryCode }

}
