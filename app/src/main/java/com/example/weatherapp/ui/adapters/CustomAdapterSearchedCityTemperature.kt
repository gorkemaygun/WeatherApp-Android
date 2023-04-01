package com.example.weatherapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherDetail
import com.example.weatherapp.databinding.ListItemSearchedCityTemperatureBinding
import com.example.weatherapp.util.AppConstants
import com.example.weatherapp.util.AppUtils

class CustomAdapterSearchedCityTemperature :
    RecyclerView.Adapter<CustomAdapterSearchedCityTemperature.ViewHolder>() {

    private var weatherDetailList = ArrayList<WeatherDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemSearchedCityTemperatureBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_searched_city_temperature, parent, false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weatherDetailList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(weatherDetailList[position])
    }

    fun setData(newWeatherDetail: List<WeatherDetail>) {
        weatherDetailList.clear()
        weatherDetailList.addAll(newWeatherDetail)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListItemSearchedCityTemperatureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(weatherDetail: WeatherDetail) {
            binding.apply {
                val iconCode = weatherDetail.icon?.replace("n", "d")
                AppUtils.setGlideImage(
                    imageWeatherSymbol,
                    AppConstants.WEATHER_API_IMAGE_ENDPOINT + "${iconCode}@4x.png"
                )
                textCityName.text =
                    "${weatherDetail.cityName?.capitalize()}, ${weatherDetail.countryName}"
                textTemperature.text = weatherDetail.temp.toString()
            }
        }
    }
}



