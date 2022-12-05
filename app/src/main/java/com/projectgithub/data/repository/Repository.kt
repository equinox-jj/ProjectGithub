package com.projectgithub.data.repository

import com.projectgithub.common.Resources
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.data.model.ResultItem
import com.projectgithub.data.preferences.ThemeDataStore
import com.projectgithub.data.source.local.dao.UserDao
import com.projectgithub.data.source.local.entity.UserEntity
import com.projectgithub.data.source.remote.network.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository constructor(
    private val apiServices: ApiServices,
    private val userDao: UserDao,
    private val themeDataStore: ThemeDataStore
) {

    companion object {
        private var INSTANCE: Repository? = null

        /**
         * @see INSTANCE: if the INSTANCE is not null, then return it.
         * if INSTANCE is null, then create the repository instance.*/
        fun getInstance(
            apiServices: ApiServices,
            userDao: UserDao,
            themeDataStore: ThemeDataStore
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(apiServices, userDao, themeDataStore)
            }
        }
    }

    /**
     * @see flow: is a builder to creating flows*/
    fun searchUser(query: String): Flow<Resources<List<ResultItem>>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.searchUser(query).items
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    fun getUserByName(username: String): Flow<Resources<DetailResponse>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.getUserByName(username)
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    fun getFollowers(username: String): Flow<Resources<List<ResultItem>>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.getFollowers(username)
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    fun getFollowing(username: String): Flow<Resources<List<ResultItem>>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.getFollowing(username)
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    val getUser: Flow<List<UserEntity>> = userDao.getUser()
    suspend fun insertUser(entity: UserEntity) = userDao.insertUser(entity)
    suspend fun deleteUser(entity: UserEntity) = userDao.deleteUser(entity)
    suspend fun deleteAllUser() = userDao.deleteAllUser()

    val getDarkModeKey = themeDataStore.getDarkModeKey
    suspend fun saveDarkModeKey(isDarkMode: Boolean) {
        themeDataStore.saveDarkModeKey(isDarkMode)
    }

}