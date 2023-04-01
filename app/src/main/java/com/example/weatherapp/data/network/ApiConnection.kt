package com.example.weatherapp.data.network

import android.util.Log.VERBOSE
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.util.AppConstants
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiConnection {

    @GET("weather")
    suspend fun getWeatherData(
        @Query("q") cityName: String,
        @Query("units") units:String = AppConstants.units,
        @Query("appid") apikey: String = AppConstants.apiKey
    ): Response<WeatherResponse>

    companion object {
        operator fun invoke(): ApiConnection {

            val baseURL = AppConstants.BASE_URL
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                    LoggingInterceptor.Builder()
                        .setLevel(Level.BASIC)
                        .log(VERBOSE)
                        .build()
                )
                .connectTimeout(1, TimeUnit.MINUTES)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiConnection::class.java)
        }
    }
}