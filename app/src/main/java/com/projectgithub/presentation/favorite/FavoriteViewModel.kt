package com.projectgithub.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.projectgithub.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val getUser = repository.getUser.asLiveData()

    fun deleteAllUser() {
        viewModelScope.launch {
            repository.deleteAllUser()
        }
    }

}