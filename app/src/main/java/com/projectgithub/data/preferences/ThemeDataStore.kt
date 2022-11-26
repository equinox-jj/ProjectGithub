package com.projectgithub.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.projectgithub.common.Constants
import com.projectgithub.common.Constants.NIGHT_MODE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.PREF_NAME)

class ThemeDataStore(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val isDarkMode = booleanPreferencesKey(NIGHT_MODE_KEY)
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