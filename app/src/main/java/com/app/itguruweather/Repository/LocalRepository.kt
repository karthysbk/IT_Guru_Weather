package com.app.itguruweather.Repository

import com.app.itguruweather.data.User
import com.app.itguruweather.data.UserDao
import com.app.itguruweather.data.remote.WeatherService
import com.app.retrofitroomhiltmvvm.utils.Resource
import java.lang.Exception
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val roomApi: UserDao
) {
    suspend fun getUserList(): Resource<List<User>> {
        return try {
            val response = roomApi.getUser()
            if (response.size > 0) {
                Resource.Success(response, null)
            } else {
                Resource.Error("No user found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Users exception")
        }
    }

    suspend fun deleteUser(user: User): Resource<Int> {
        return try {
            val response = roomApi.deleteUser(user)
            if (response != -1) {
                Resource.Success(response, "Deleted Successfully")
            } else {
                Resource.Error("User not deleted Successfully")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "User not deleted Successfully")
        }
    }

    suspend fun insertUser(user: User): Resource<Long> {
        return try {
            val response = roomApi.insertUser(user)
            if (response != -1L) {
                Resource.Success(response, "User added Successfully")
            } else {
                Resource.Error("User not added Successfully")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "User not added Successfully")
        }
    }
}