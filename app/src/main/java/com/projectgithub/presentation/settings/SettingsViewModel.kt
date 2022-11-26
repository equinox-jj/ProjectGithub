package com.projectgithub.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.projectgithub.data.repository.Repository
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: Repository): ViewModel() {

    val getDarkModeKey = repository.getDarkModeKey.asLiveData()

    fun saveDarkModeKey(isDarkMode: Boolean) {
        viewModelScope.launch {
            repository.saveDarkModeKey(isDarkMode)
        }
    }
}