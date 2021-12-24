package com.app.itguruweather.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.itguruweather.data.LocalDatabase
import com.app.itguruweather.data.UserDao
import com.app.itguruweather.data.remote.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDatabase =
        Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "User_database"
        ).build()

    @Provides
    @Singleton
    fun provideUserDao(localDatabase: LocalDatabase): UserDao = localDatabase.userDao

    @Provides
    @Singleton
    fun provideWeatherService(): WeatherService {
        return WeatherService.create()
    }

}