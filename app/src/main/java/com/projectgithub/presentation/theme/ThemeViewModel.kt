package com.projectgithub.presentation.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.projectgithub.data.preferences.ThemeDataStore
import kotlinx.coroutines.launch

class ThemeViewModel(private val themeDataStore: ThemeDataStore): ViewModel() {

    val getDarkModeKey = themeDataStore.getDarkModeKey.asLiveData()

    fun saveDarkModeKey(isDarkMode: Boolean) {
        viewModelScope.launch {
            themeDataStore.saveDarkModeKey(isDarkMode)
        }
    }
}