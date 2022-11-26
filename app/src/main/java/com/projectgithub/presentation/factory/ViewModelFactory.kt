package com.projectgithub.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectgithub.data.repository.Repository
import com.projectgithub.di.Injection
import com.projectgithub.presentation.detail.DetailViewModel
import com.projectgithub.presentation.followers.FollowersViewModel
import com.projectgithub.presentation.following.FollowingViewModel
import com.projectgithub.presentation.home.HomeViewModel

class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> {
                FollowersViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> {
                FollowingViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.providesRepository())
            }
        }
    }
}