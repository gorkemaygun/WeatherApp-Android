package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.data.dao.WeatherDetailDatabase
import com.example.weatherapp.data.network.ApiConnection
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.viewModel.WeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeatherApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))

        bind() from singleton { ApiConnection() }
        bind() from singleton { WeatherRepository(instance(), instance()) }
        bind() from provider { WeatherDetailDatabase(instance()) }
        bind() from provider { WeatherViewModelFactory(instance()) }

    }
}