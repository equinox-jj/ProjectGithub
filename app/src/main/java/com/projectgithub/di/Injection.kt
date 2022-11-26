package com.projectgithub.di

import com.projectgithub.data.repository.Repository
import com.projectgithub.data.source.remote.network.ApiConfig

object Injection {

    fun providesRepository(): Repository {
        val apiServices = ApiConfig.apiServices
        return Repository.getInstance(apiServices)
    }

}