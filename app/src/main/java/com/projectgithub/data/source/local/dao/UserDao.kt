package com.projectgithub.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.projectgithub.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getUser(): Flow<List<UserEntity>>

    @Insert
    suspend fun insertUser(entity: UserEntity)

    @Delete
    suspend fun deleteUser(entity: UserEntity)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUser()

}