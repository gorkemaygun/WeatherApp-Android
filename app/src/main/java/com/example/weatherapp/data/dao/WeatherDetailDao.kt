package com.example.weatherapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.model.WeatherDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherDetail(weatherDetail: WeatherDetail)

    @Delete
    suspend fun deleteWeatherDetail(weatherDetail: WeatherDetail)

    @Query("SELECT * FROM weather_detail ORDER BY cityName DESC")
    suspend fun getAllWeatherDetail(): List<WeatherDetail>

    @Query("DELETE FROM weather_detail")
    suspend fun deleteAllWeatherDetail()

    @Query("SELECT * FROM weather_detail WHERE cityName = :cityName")
    suspend fun getWeatherByCityName(cityName : String): WeatherDetail?

}