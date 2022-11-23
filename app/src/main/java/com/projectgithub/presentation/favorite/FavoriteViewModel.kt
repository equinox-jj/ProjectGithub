package com.projectgithub.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.projectgithub.data.repository.LocalRepository
import kotlinx.coroutines.launch

class FavoriteViewModel constructor(private val localRepository: LocalRepository) : ViewModel() {

    val getUser = localRepository.getUser.asLiveData()

    fun deleteAllUser() {
        viewModelScope.launch {
            localRepository.deleteAllUser()
        }
    }

}