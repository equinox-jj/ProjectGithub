package com.projectgithub.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectgithub.common.Resources
import com.projectgithub.data.Repository
import com.projectgithub.data.model.ResultItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel constructor(private val repository: Repository) : ViewModel() {

    private val _state = MutableLiveData<Resources<List<ResultItem>>>()
    val state: LiveData<Resources<List<ResultItem>>> = _state

    private var searchJob: Job? = null

    fun searchUser(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(800L)
            repository.searchUser(query)
                .onStart {
                    _state.value = Resources.Loading()
                }
                .catch { error ->
                    error.message?.let { message ->
                        _state.value = Resources.Error(message)
                    }
                }
                .collect { result ->
                    result.data?.let { data ->
                        _state.value = Resources.Success(data)
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}