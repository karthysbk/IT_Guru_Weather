package com.app.itguruweather.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [User::class],
    version = 1
)
abstract class LocalDatabase : RoomDatabase() {

    abstract val userDao: UserDao

}