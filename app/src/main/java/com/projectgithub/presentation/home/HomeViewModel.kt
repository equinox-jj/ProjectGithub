package com.projectgithub.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectgithub.common.Resources
import com.projectgithub.data.model.ResultItem
import com.projectgithub.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

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
                .flatMapLatest { repository.searchUser(it) }
                .collect {
                    _state.value = it
                }
        }
    }
}