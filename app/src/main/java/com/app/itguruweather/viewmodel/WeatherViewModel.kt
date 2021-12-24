package com.app.itguruweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.itguruweather.Repository.RemoteRepository
import com.app.itguruweather.data.remote.WeatherData
import com.app.retrofitroomhiltmvvm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    sealed class WeatherUiState {
        class Success(val data: WeatherData?, val message: String) : WeatherUiState()
        class Weather(
            val temp: String?,
            val weatherType: String?,
            val humidity: String?,
            val windSpeed: String?
        ) : WeatherUiState()

        class Failure(val message: String) : WeatherUiState()
        object Loading : WeatherUiState()
        object Empty : WeatherUiState()
    }

    private val _weatherUiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Empty)
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    init {
        callWeatherData()
    }

    fun callWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = remoteRepository.getWeatherReport()) {
                is Resource.Success -> _weatherUiState.value =
                    WeatherUiState.Weather(
                        result.data?.current?.temp.toString(),
                        result.data?.current?.weather?.get(0)?.main.toString(),
                        result.data?.current?.humidity.toString(),
                        result.data?.current?.wind_speed.toString(),
                    )
                is Resource.Error -> _weatherUiState.value =
                    WeatherUiState.Failure(result.message ?: "Error Occurred")
                is Resource.Loading -> _weatherUiState.value = WeatherUiState.Loading
            }
        }
    }
}