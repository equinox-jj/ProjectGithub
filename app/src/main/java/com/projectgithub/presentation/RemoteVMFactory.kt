package com.projectgithub.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.data.repository.RemoteRepository
import com.projectgithub.presentation.followers.FollowersViewModel
import com.projectgithub.presentation.following.FollowingViewModel
import com.projectgithub.presentation.home.HomeViewModel

class RemoteVMFactory(private val remoteRepository: RemoteRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(HomeViewModel::class.java)) -> {
                HomeViewModel(remoteRepository) as T
            }
            (modelClass.isAssignableFrom(FollowingViewModel::class.java)) -> {
                FollowingViewModel(remoteRepository) as T
            }
            (modelClass.isAssignableFrom(FollowersViewModel::class.java)) -> {
                FollowersViewModel(remoteRepository) as T
            }
            else -> throw RuntimeException("Illegal View Model Not Found.")
        }
    }
}