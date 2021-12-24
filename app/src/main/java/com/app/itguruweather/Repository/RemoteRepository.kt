package com.app.itguruweather.Repository

import android.util.Log
import com.app.itguruweather.data.remote.WeatherData
import com.app.itguruweather.data.remote.WeatherResponse
import com.app.itguruweather.data.remote.WeatherService
import com.app.retrofitroomhiltmvvm.utils.Resource
import java.lang.Exception
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val remoteApi: WeatherService
) {
    suspend fun getWeatherReport(): Resource<WeatherResponse> {
        return try {
            val response = remoteApi.getWeatherData(12.9082847623315,77.65197822993314,"imperial","b143bb707b2ee117e62649b358207d3e")
            val result = response.body()
            Log.e("bodyy",result.toString())
            if (response.isSuccessful && result != null) {
                Resource.Success(response.body()!!, "Success")
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Log.e("RemoteEx",e.message.toString())
            Resource.Error(e.message ?: "Weather exception occurred")
        }
    }
}
/*
 private val ABASE_URL =
            "data/2.5/onecall?lat=12.9082847623315&lon=77.65197822993314&units=imperial&appid=b143bb707b2ee117e62649b358207d3e/"
 */