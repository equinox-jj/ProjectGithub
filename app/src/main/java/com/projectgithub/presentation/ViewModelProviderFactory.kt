package com.projectgithub.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.data.Repository
import com.projectgithub.presentation.detail.DetailViewModel
import com.projectgithub.presentation.followers.FollowersViewModel
import com.projectgithub.presentation.following.FollowingViewModel
import com.projectgithub.presentation.home.HomeViewModel

class ViewModelProviderFactory(private val repository: Repository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(HomeViewModel::class.java)) -> {
                HomeViewModel(repository) as T
            }
            (modelClass.isAssignableFrom(DetailViewModel::class.java)) -> {
                DetailViewModel(repository) as T
            }
            (modelClass.isAssignableFrom(FollowingViewModel::class.java)) -> {
                FollowingViewModel(repository) as T
            }
            (modelClass.isAssignableFrom(FollowersViewModel::class.java)) -> {
                FollowersViewModel(repository) as T
            }
            else -> throw RuntimeException("Illegal View Model Not Found.")
        }
    }
}