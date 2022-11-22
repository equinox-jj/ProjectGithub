package com.projectgithub.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.projectgithub.common.Constants.NIGHT_MODE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ThemeDataStore(private val dataStore: DataStore<Preferences>) {

    private val isDarkMode = booleanPreferencesKey(NIGHT_MODE_KEY)

    companion object {
        @Volatile
        private var INSTANCE: ThemeDataStore? = null

        fun getInstance(dataStore: DataStore<Preferences>): ThemeDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemeDataStore(dataStore)
                INSTANCE = instance
                return instance
            }
        }
    }

    suspend fun saveDarkModeKey(darkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[isDarkMode] = darkMode
        }
    }

    val getDarkModeKey: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            return@map preferences[isDarkMode] ?: false
        }

}