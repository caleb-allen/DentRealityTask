package xyz.wim.dentrealitytask.data

import com.squareup.moshi.Json

data class Country(
    val timezones: List<String>,
    val latlng: List<Double>,
    val name: String,
    @Json(name = "country_code") val countryCode: String,
    val capital: String
)
