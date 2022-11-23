package com.projectgithub.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.data.repository.LocalRepository
import com.projectgithub.presentation.favorite.FavoriteViewModel

class LocalVMFactory(
    private val localRepository: LocalRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) -> {
                FavoriteViewModel(localRepository) as T
            }
            else -> throw RuntimeException("Illegal View Model Not Found.")
        }
    }
}