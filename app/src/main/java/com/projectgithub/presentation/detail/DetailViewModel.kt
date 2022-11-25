package com.projectgithub.presentation.detail

import androidx.lifecycle.*
import com.projectgithub.common.Resources
import com.projectgithub.data.model.DetailResponse
import com.projectgithub.data.repository.Repository
import com.projectgithub.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel constructor(private val repository: Repository, ) : ViewModel() {

    private val _state = MutableLiveData<Resources<DetailResponse>>()
    val state: LiveData<Resources<DetailResponse>> = _state

    val getUser = repository.getUser.asLiveData()

    fun onRefresh(username: String) {
        getUserByName(username)
    }

    fun getUserByName(username: String) {
        viewModelScope.launch {
            repository.getUserByName(username)
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
            repository.insertUser(entity)
        }
    }

    fun deleteUser(entity: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(entity)
        }
    }

}