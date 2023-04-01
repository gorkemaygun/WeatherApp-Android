package com.example.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_detail")
data class WeatherDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    var cityName: String? = null,
    var temp: Int? = null,
    var icon: String? = null,
    var main: String? = null,
    var countryName: String? = null,
    var temp_min: Int? = null,
    var temp_max: Int? = null
) {
}