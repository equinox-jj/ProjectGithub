package com.projectgithub.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectgithub.common.Resources
import com.projectgithub.data.model.ResultItem
import com.projectgithub.data.repository.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel constructor(private val remoteRepository: RemoteRepository) : ViewModel() {

    private val _state = MutableLiveData<Resources<List<ResultItem>>>()
    val state: LiveData<Resources<List<ResultItem>>> = _state

    fun onRefresh(query: String) {
        searchUser(query)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun searchUser(query: String) {
        viewModelScope.launch {
            flowOf(query)
                .debounce(800)
                .filter { it.trim().isEmpty().not() }
                .distinctUntilChanged()
                .flatMapLatest { remoteRepository.searchUser(it) }
                .flowOn(Dispatchers.Default)
                .collect {
                    if (it is Resources.Success) {
                        it.data
                    }
                    _state.value = it
                }
        }
    }
}