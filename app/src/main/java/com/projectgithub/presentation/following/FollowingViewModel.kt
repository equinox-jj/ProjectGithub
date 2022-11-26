package com.projectgithub.presentation.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectgithub.common.Resources
import com.projectgithub.data.model.ResultItem
import com.projectgithub.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _state = MutableLiveData<Resources<List<ResultItem>>>()
    val state: LiveData<Resources<List<ResultItem>>> = _state

    fun onRefresh(username: String) {
        getFollowing(username)
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            repository.getFollowing(username)
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