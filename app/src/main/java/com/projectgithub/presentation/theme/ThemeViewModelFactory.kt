package com.projectgithub.presentation.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.data.preferences.ThemeDataStore

class ThemeViewModelFactory(private val themeDataStore: ThemeDataStore): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(ThemeViewModel::class.java)) -> {
                ThemeViewModel(themeDataStore) as T
            }
            else -> throw RuntimeException("Illegal View Model Not Found.")
        }
    }
}