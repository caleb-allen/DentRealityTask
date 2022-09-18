package xyz.wim.dentrealitytask.data

interface CountryRepository {
    suspend fun getCountries(): List<Country>

    suspend fun getCountry(countryCode: String): Country?

    /**
     * Mark the country with [countryCode] as the user's favorite.
     * Up to one favorite country may be set at a time. Setting a favorite country
     * will overwrite a previously set favorite, if any.
     *
     * @param countryCode the country code to be set as favorite. Passing null will
     * remove a previously set favorite, if any.
     */
    fun setHome(countryCode: String?)
}