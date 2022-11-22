package com.projectgithub.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectgithub.common.Resources
import com.projectgithub.data.Repository
import com.projectgithub.data.model.ResultItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel constructor(private val repository: Repository) : ViewModel() {

    private val _state = MutableLiveData<Resources<List<ResultItem>>>()
    val state: LiveData<Resources<List<ResultItem>>> = _state

    fun onRefresh(query: String) {
        searchUser(query)
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun searchUser(query: String) {
        viewModelScope.launch {
            flowOf(query)
                .debounce(300)
                .filter { it.trim().isEmpty().not() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    repository.searchUser(query)
                        .catch {
                            _state.value = Resources.Error(it.message ?: "")
                        }
                }
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