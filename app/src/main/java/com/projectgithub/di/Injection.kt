package com.projectgithub.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.projectgithub.data.preferences.ThemeDataStore
import com.projectgithub.data.repository.Repository
import com.projectgithub.data.source.local.database.UserDatabase
import com.projectgithub.data.source.remote.network.ApiConfig

object Injection {

    fun providesRepository(context: Context, dataStore: DataStore<Preferences>): Repository {
        val database = UserDatabase.getInstance(context)
        val apiServices = ApiConfig.apiServices
        val userDao = database.userDao()
        val themeDataStore = ThemeDataStore.getInstance(dataStore)
        return Repository(apiServices, userDao, themeDataStore)
    }

}