package com.example.weatherapp.data.repository

import androidx.annotation.WorkerThread
import com.example.weatherapp.data.dao.WeatherDetailDao
import com.example.weatherapp.data.dao.WeatherDetailDatabase
import com.example.weatherapp.data.model.WeatherDetail
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.ApiConnection
import com.example.weatherapp.data.network.SafeApiRequest
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val db: WeatherDetailDatabase,
    private var api: ApiConnection
) : SafeApiRequest() {

    // val allWeathers: Flow<List<WeatherDetail>> = db.getWeatherDetailDao().getAllWeatherDetail()

    @WorkerThread
    suspend fun insertWeatherDetail(weatherDetail: WeatherDetail) {
        db.getWeatherDetailDao().insertWeatherDetail(weatherDetail)
    }

    @WorkerThread
    suspend fun deleteAllWeatherData() {
        db.getWeatherDetailDao().deleteAllWeatherDetail()
    }

    @WorkerThread
    suspend fun deleteWeatherData(weatherDetail: WeatherDetail) {
        db.getWeatherDetailDao().deleteWeatherDetail(weatherDetail)
    }

    @WorkerThread
    suspend fun findCityWeather(cityName: String): WeatherResponse = apiRequest {
        api.getWeatherData(cityName)
    }

    suspend fun fetchWeatherDetail(cityName: String): WeatherDetail? =
        db.getWeatherDetailDao().getWeatherByCityName(cityName)

    suspend fun fetchAllWeatherDetails():List<WeatherDetail> =
        db.getWeatherDetailDao().getAllWeatherDetail()
}