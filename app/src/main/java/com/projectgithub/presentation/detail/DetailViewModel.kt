package com.projectgithub.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectgithub.common.Resources
import com.projectgithub.data.Repository
import com.projectgithub.data.model.DetailResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel constructor(private val repository: Repository) : ViewModel() {

    private val _state = MutableLiveData<Resources<DetailResponse>>()
    val state: LiveData<Resources<DetailResponse>> = _state

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

}