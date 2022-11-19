package com.projectgithub.data.network

import com.projectgithub.common.Constants.DETAIL_API
import com.projectgithub.common.Constants.FOLLOWERS_API
import com.projectgithub.common.Constants.FOLLOWING_API
import com.projectgithub.common.Constants.SEARCH_API
import com.projectgithub.common.Constants.TOKEN
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET(SEARCH_API)
    @Headers("Authorization: token $TOKEN")
    suspend fun searchUser(
        @Query("q") query: String,
    ): SearchResponse

    @GET(DETAIL_API)
    @Headers("Authorization: token $TOKEN")
    suspend fun getUserByName(
        @Path("username") username: String,
    ): DetailResponse

    @GET(FOLLOWERS_API)
    @Headers("Authorization: token $TOKEN")
    suspend fun getFollowers(
        @Path("username") username: String,
    ): DetailResponse

    @GET(FOLLOWING_API)
    @Headers("Authorization: token $TOKEN")
    suspend fun getFollowing(
        @Path("username") username: String,
    ): DetailResponse

}