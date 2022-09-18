package xyz.wim.dentrealitytask.data

interface CountryRepository {
    suspend fun getCountries(): List<Country>

    suspend fun getCountry(countryCode: String): Country

}