package com.example.weatherapp.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherDetail
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.ListItemSearchedCityTemperatureBinding
import com.example.weatherapp.ui.adapters.CustomAdapterSearchedCityTemperature
import com.example.weatherapp.ui.viewModel.WeatherViewModel
import com.example.weatherapp.ui.viewModel.WeatherViewModelFactory
import com.example.weatherapp.util.AppConstants
import com.example.weatherapp.util.AppUtils
import com.example.weatherapp.util.EventObserver
import com.example.weatherapp.util.State
import com.example.weatherapp.util.hide
import com.example.weatherapp.util.show
import com.example.weatherapp.util.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private lateinit var dataBind: ActivityMainBinding
    private val factory: WeatherViewModelFactory by instance()
    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this, factory)[WeatherViewModel::class.java]
    }
    private lateinit var customAdapterSearchedCityTemperature: CustomAdapterSearchedCityTemperature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupUI()
        observeApiCall()
    }

    private fun setupUI() {
        initializeRecyclerView()
        dataBind.inputFindCityWeather.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.fetchWeatherDetailFromDb((view as EditText).text.toString())
                viewModel.fetchAllWeatherDetailsFromDb()
            }
            false
        }
    }

    private fun initializeRecyclerView() {
        customAdapterSearchedCityTemperature = CustomAdapterSearchedCityTemperature()
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dataBind.recyclerViewSearchedCityTemperature.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterSearchedCityTemperature
        }
    }

    private fun observeApiCall() {
        viewModel.weatherLiveData.observe(this, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    dataBind.textLabelSearchForCity.hide()
                    dataBind.imageCity.hide()
                    dataBind.constraintLayoutShowingTemp.show()
                    dataBind.inputFindCityWeather.text?.clear()
                    state.data.let { weatherDetail ->
                        val iconCode = weatherDetail.icon?.replace("n", "d")
                        AppUtils.setGlideImage(
                            dataBind.imageWeatherSymbol,
                            AppConstants.WEATHER_API_IMAGE_ENDPOINT + "${iconCode}@4x.png"
                        )
                        changeBgAccToTemp(iconCode)
                        dataBind.textTemperature.text = weatherDetail.temp?.toString()
                        dataBind.textCityName.text =
                            "${weatherDetail.cityName?.capitalize()}, ${weatherDetail.countryName}"
                        dataBind.textTempDescription.text = weatherDetail.main.toString()
                        dataBind.textLowTemp.text = weatherDetail.temp_min.toString()
                        dataBind.textHighTemp.text = weatherDetail.temp_max.toString()
                    }
                }
                is State.Error -> {
                    showToast(state.message)
                }
            }

        })
        viewModel.weatherDetailListLiveData.observe(this, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    if (state.data.isEmpty()) {
                        dataBind.recyclerViewSearchedCityTemperature.hide()
                    } else {
                        dataBind.recyclerViewSearchedCityTemperature.show()
                        customAdapterSearchedCityTemperature.setData(state.data)
                    }
                }
                is State.Error -> {
                    showToast(state.message)
                }
            }

        })
    }

    private fun changeBgAccToTemp(iconCode: String?) {
        iconCode?.replace("n", "d")
        when (iconCode) {
            "01d", "02d", "03d" -> dataBind.imageWeatherHumanReaction.setImageResource(R.drawable.sunny_day)
            "04d", "09d", "10d", "11d" -> dataBind.imageWeatherHumanReaction.setImageResource(R.drawable.raining)
            "13d", "50d" -> dataBind.imageWeatherHumanReaction.setImageResource(R.drawable.snowfalling)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.converter_menu, menu)
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.item_fahrenheit -> {
                AppConstants.units = "imperial"
                viewModel.findCityWeather(dataBind.textCityName.text.toString())
                dataBind.textLabelDegree.text = "\u2109"
                dataBind.textLabelDegreeSymbol.text = "\u2109"
                dataBind.textLabelDegreeSymbol2.text = "\u2109"
                // viewModel.citiesList.forEach{cityName ->
                //     val weatherDetailList : ArrayList<WeatherDetail> = ArrayList<WeatherDetail>()
                //     weatherDetailList.add(viewModel.findCityWeather(cityName) as WeatherDetail)
                //     customAdapterSearchedCityTemperature.setData(weatherDetailList)
                // }
                observeApiCall()
            }
            R.id.item_celsius -> {
                AppConstants.units = "metric"
                viewModel.findCityWeather(dataBind.textCityName.text.toString())
                dataBind.textLabelDegree.text = "\u2103"
                dataBind.textLabelDegreeSymbol.text = "\u2103"
                dataBind.textLabelDegreeSymbol2.text = "\u2103"
            }
        }
        return true
    }
}