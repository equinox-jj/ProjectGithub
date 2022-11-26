package com.projectgithub.common

import com.projectgithub.BuildConfig

object Constants {
    const val BASE_URL = "https://api.github.com/"
    const val TOKEN = BuildConfig.API_KEY
    const val SEARCH_API = "search/users"
    const val DETAIL_API = "users/{username}"
    const val FOLLOWERS_API = "users/{username}/followers"
    const val FOLLOWING_API = "users/{username}/following"

    const val NAME_ARGS = "username"

}