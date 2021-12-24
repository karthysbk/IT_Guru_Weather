package com.app.itguruweather.data

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM USER_TABLE")
    suspend fun getUser(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User) : Long

    @Delete
    suspend fun deleteUser(user: User): Int

    @Query("DELETE FROM USER_TABLE WHERE id = :userId")
    suspend fun deleteUserById(userId: Long)
}