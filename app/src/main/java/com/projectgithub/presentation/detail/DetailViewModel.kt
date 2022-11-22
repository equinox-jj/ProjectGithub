package com.projectgithub.presentation.detail

import androidx.lifecycle.*
import com.projectgithub.common.Resources
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.data.repository.LocalRepository
import com.projectgithub.data.repository.RemoteRepository
import com.projectgithub.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _state = MutableLiveData<Resources<DetailResponse>>()
    val state: LiveData<Resources<DetailResponse>> = _state

    val getUser = localRepository.getUser.asLiveData()

    fun onRefresh(username: String) {
        getUserByName(username)
    }

    fun getUserByName(username: String) {
        viewModelScope.launch {
            remoteRepository.getUserByName(username)
                .onStart {
                    _state.value = Resources.Loading()
                }
                .catch { error ->
                    error.message?.let { message ->
                        _state.value = Resources.Error(message)
                    }
                }
                .collect { result ->
                    result.data?.let { response ->
                        _state.value = Resources.Success(response)
                    }
                }
        }
    }

    fun insertUser(entity: UserEntity) {
        viewModelScope.launch {
            localRepository.insertUser(entity)
        }
    }

    fun deleteUser(entity: UserEntity) {
        viewModelScope.launch {
            localRepository.deleteUser(entity)
        }
    }

}