package com.projectgithub.data

import com.projectgithub.common.Resources
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.data.model.ResultItem
import com.projectgithub.data.source.local.database.UserDatabase
import com.projectgithub.data.source.local.entity.UserEntity
import com.projectgithub.data.source.remote.network.ApiServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class Repository constructor(
    private val apiServices: ApiServices,
    private val userDb: UserDatabase
    ) {

    fun searchUser(query: String): Flow<Resources<List<ResultItem>>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.searchUser(query).items
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        } catch (e: HttpException) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }

    fun getUserByName(username: String): Flow<Resources<DetailResponse>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.getUserByName(username)
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        } catch (e: HttpException) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }

    fun getFollowers(username: String): Flow<Resources<List<ResultItem>>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.getFollowers(username)
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        } catch (e: HttpException) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }

    fun getFollowing(username: String): Flow<Resources<List<ResultItem>>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.getFollowing(username)
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        } catch (e: HttpException) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }

    val getUser: Flow<List<UserEntity>> = userDb.userDao().getUser()
    suspend fun insertUser(entity: UserEntity) = userDb.userDao().insertUser(entity)
    suspend fun deleteUser(entity: UserEntity) = userDb.userDao().deleteUser(entity)
    suspend fun deleteAllUser() = userDb.userDao().deleteAllUser()

}