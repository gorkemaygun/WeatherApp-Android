package com.example.weatherapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherDetail
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.util.Event
import com.example.weatherapp.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private lateinit var weatherResponse: WeatherResponse
    private lateinit var weatherDetailList: List<WeatherDetail>
    var citiesList:HashSet<String> = HashSet<String>()

    private val _weatherLiveData = MutableLiveData<Event<State<WeatherDetail>>>()
    val weatherLiveData: LiveData<Event<State<WeatherDetail>>>
        get() = _weatherLiveData

    private val _weatherDetailListLiveData = MutableLiveData<Event<State<List<WeatherDetail>>>>()
    val weatherDetailListLiveData: LiveData<Event<State<List<WeatherDetail>>>>
        get() = _weatherDetailListLiveData

     fun findCityWeather(cityName: String) {
        _weatherLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherResponse = repository.findCityWeather(cityName)
                // deleteAllWeatherDetails()
                citiesList.add(cityName)
                addWeatherDetailIntoDb(weatherResponse)
                withContext(Dispatchers.Main) {
                    val weatherDetail = WeatherDetail()
                    weatherDetail.icon = weatherResponse.weather.first().icon
                    weatherDetail.cityName = weatherResponse.cityName
                    weatherDetail.countryName = weatherResponse.sys.country
                    weatherDetail.temp = weatherResponse.main.temp.roundToInt()
                    weatherDetail.main = weatherResponse.weather.first().main
                    weatherDetail.temp_max = weatherResponse.main.temp_min.roundToInt()
                    weatherDetail.temp_min = weatherResponse.main.temp_max.roundToInt()
                    _weatherLiveData.postValue(Event(State.success(weatherDetail)))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            }
        }

    }

    suspend fun addWeatherDetailIntoDb(weatherResponse: WeatherResponse) {
        val weatherDetail = WeatherDetail(
            weatherResponse.id,
            weatherResponse.cityName,
            weatherResponse.main.temp.roundToInt(),
            weatherResponse.weather.first().icon,
            weatherResponse.weather.first().main,
            weatherResponse.sys.country,
            weatherResponse.main.temp_min.roundToInt(),
            weatherResponse.main.temp_max.roundToInt()
        )
        repository.insertWeatherDetail(weatherDetail)
    }

    fun fetchWeatherDetailFromDb(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetail = repository.fetchWeatherDetail(cityName.toLowerCase())
            withContext(Dispatchers.Main) {
                if (weatherDetail != null) {
                    _weatherLiveData.postValue(Event(State.success(weatherDetail)))
                } else {
                    findCityWeather(cityName)
                }
            }
        }
    }

    fun fetchAllWeatherDetailsFromDb(){
        viewModelScope.launch(Dispatchers.IO) {
            weatherDetailList = repository.fetchAllWeatherDetails()
            withContext(Dispatchers.Main) {
                _weatherDetailListLiveData.postValue(
                    Event(State.success(weatherDetailList))
                )
            }
        }
    }

    fun deleteAllWeatherDetails(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllWeatherData()
        }
    }
}




