package xyz.wim.dentrealitytask.data

interface CountryRepository {
    fun getCountries(): List<Country>

    fun getCountry(countryCode: String): Country?

    fun setFavorite(countryCode: String)
}