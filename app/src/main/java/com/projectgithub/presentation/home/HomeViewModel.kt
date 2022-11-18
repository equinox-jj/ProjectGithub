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

    private val _search = MutableLiveData<Resources<List<ResultItem>>>()
    val search: LiveData<Resources<List<ResultItem>>> = _search

    private var searchJob: Job? = null

    fun searchUser(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(600)
            repository.searchUser(query)
                .onStart {
                    _search.value = Resources.Loading()
                }
                .catch { error ->
                    error.message?.let { message ->
                        _search.value = Resources.Error(message)
                    }
                }
                .collect { result ->
                    result.data?.let { data ->
                        _search.value = Resources.Success(data)
                    }
                }
        }
    }

}