package com.projectgithub.data

import com.projectgithub.common.Resources
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.data.model.ResultItem
import com.projectgithub.data.network.ApiConfig
import com.projectgithub.data.network.ApiServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository constructor(private val apiServices: ApiServices) {

    fun searchUser(query: String): Flow<Resources<List<ResultItem>>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.searchUser(query)
            emit(Resources.Success(result))
        } catch (e: Exception) {
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
        }
    }

    fun getFollowers(username: String): Flow<Resources<DetailResponse>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.getFollowers(username)
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }

    fun getFollowing(username: String): Flow<Resources<DetailResponse>> = flow {
        emit(Resources.Loading())
        try {
            val result = apiServices.getFollowing(username)
            emit(Resources.Success(result))
        } catch (e: Exception) {
            emit(Resources.Error(e.localizedMessage ?: ""))
        }
    }

}