package com.projectgithub.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectgithub.common.Resources
import com.projectgithub.data.model.ResultItem
import com.projectgithub.data.repository.Repository
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

    /**
     * @see debounce: operator used with a time constant. to prevent api call whenever user typed.
     * @see filter: operator used to filter unwanted string like empty string and to avoid the
     * unnecessary network call.
     * @see distinctUntilChanged: operator used to avoid duplicate network calls data.
     * if user typed "abc" and deleted "c" and typed "c" again. if the network call already going on
     * with "abc" it will not make the duplicate again with the search query.
     * @see flatMapLatest: operator used to  avoid the network call results
     * which are not needed more for displaying to the user.
     * @see collect: operator used to collect the value from the emitter.
     * @see _state: is the consumer to consume the values from the stream.*/
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun searchUser(query: String) {
        viewModelScope.launch {
            flowOf(query)
                .debounce(600)
                .filter { it.trim().isEmpty().not() }
                .distinctUntilChanged()
                .flatMapLatest { repository.searchUser(it) }
                .collect {
                    _state.value = it
                }
        }
    }
}