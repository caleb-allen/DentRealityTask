package xyz.wim.dentrealitytask.data

interface CountryRepository {
    fun getCountries(): List<Country>
}