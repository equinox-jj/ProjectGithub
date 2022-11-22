package com.projectgithub.data.repository

import com.projectgithub.data.source.local.database.UserDatabase
import com.projectgithub.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class LocalRepository constructor(private val userDb: UserDatabase) {

    val getUser: Flow<List<UserEntity>> = userDb.userDao().getUser()
    suspend fun insertUser(entity: UserEntity) = userDb.userDao().insertUser(entity)
    suspend fun deleteUser(entity: UserEntity) = userDb.userDao().deleteUser(entity)
    suspend fun deleteAllUser() = userDb.userDao().deleteAllUser()

}