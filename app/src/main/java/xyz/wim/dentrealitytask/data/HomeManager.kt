package xyz.wim.dentrealitytask.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class HomeManager(context: Context, private val countryRepo: CountryRepository) {
    private val sharedPrefs = context.getSharedPreferences(HOME_SHARED_PREFS, Context.MODE_PRIVATE)

    /**
     * Mark the country with [countryCode] as the user's home.
     * Up to one favorite country may be set at a time. Setting a favorite country
     * will overwrite a previously set favorite, if any.
     *
     * @param countryCode the country code to be set as favorite. Passing null will
     * remove a previously set favorite, if any.
     */
    fun setHome(countryCode: String?) {
        Timber.d("Set %s as country code", countryCode)
        with(sharedPrefs.edit()) {
            putString(KEY_HOME_COUNTRY_CODE, countryCode)
            apply()
        }
    }

    /**
     * Get user's the country code, if any, or null if otherwise
     */
    suspend fun getHome(): String? {
        return withContext(Dispatchers.IO) {
            sharedPrefs.getString(KEY_HOME_COUNTRY_CODE, null)
        }
    }


    /**
     * Get the relationship between [countryCode] and a user's home country
     */
    suspend fun getHomeDetails(countryCode: String): HomeDetails {
        return when (val home = getHome()) {
            null -> HomeDetails.None
            // when the details requested for are for the user's home country
            countryCode -> HomeDetails.Home
            // when the details requested are for another country
            else -> {
                val distance = calculateDistance(home, countryCode)
                val homeName = countryRepo.getCountry(home).name
                HomeDetails.DistanceFromHome(homeName, distance)
            }
        }
    }

    /**
     * Calculate the distance between [countryCodeA] and [countryCodeB]
     *
     * @return the distance in meters
     */
    private suspend fun calculateDistance(countryCodeA: String, countryCodeB: String): Float {
        return withContext(Dispatchers.Default) {
            val countryA = countryRepo.getCountry(countryCodeA)
            val countryB = countryRepo.getCountry(countryCodeB)

            // Haversine formula, based on https://stackoverflow.com/a/837957/3408710
            val earthRadius = 6371000.0 //meters
            val distLat = Math.toRadians(countryB.lat() - countryA.lat())
            val distLon = Math.toRadians(countryB.lon() - countryA.lon())
            val a = sin(distLat / 2) *
                    sin(distLat / 2) +
                    cos(Math.toRadians(countryA.lat())) *
                    cos(Math.toRadians(countryB.lat())) *
                    sin(distLon / 2) * sin(distLon / 2)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            (earthRadius * c).toFloat()
        }
    }


    /**
     * [HomeDetails] represents the set of possible relations which a given country has to a user's
     * home
     */
    sealed class HomeDetails {
        /**
         * The country has no relation to home.
         */
        object None : HomeDetails()

        /**
         * The country is the user's home.
         */
        object Home : HomeDetails()

        /**
         * The country is [distance] away from home.
         */
        data class DistanceFromHome(val name: String, val distance: Float) : HomeDetails()
    }

    companion object {
        private const val KEY_HOME_COUNTRY_CODE = "key_home_country_code"
        private const val HOME_SHARED_PREFS = "home_shared_prefs"
    }
}
