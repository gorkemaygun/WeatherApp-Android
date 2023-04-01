package com.example.weatherapp.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.model.WeatherDetail
import kotlinx.coroutines.CoroutineScope

@Database(entities = [WeatherDetail::class], version = 3, exportSchema = false)
abstract class WeatherDetailDatabase : RoomDatabase() {

    abstract fun getWeatherDetailDao(): WeatherDetailDao

    companion object {

        @Volatile
        private var INSTANCE: WeatherDetailDatabase? = null

        operator fun invoke(context: Context): WeatherDetailDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDetailDatabase::class.java,
                    "weather_detail_database"
                ).build()
                INSTANCE =instance
                instance
            }
        }
    }
}