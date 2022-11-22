package com.projectgithub.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.data.repository.LocalRepository
import com.projectgithub.data.repository.RemoteRepository
import com.projectgithub.presentation.detail.DetailViewModel

class LocalVMFactory(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(DetailViewModel::class.java)) -> {
                DetailViewModel(remoteRepository, localRepository) as T
            }
            else -> throw RuntimeException("Illegal View Model Not Found.")
        }
    }
}