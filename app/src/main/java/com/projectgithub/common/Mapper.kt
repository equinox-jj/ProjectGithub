package com.projectgithub.common

import com.projectgithub.data.model.ResultItem
import com.projectgithub.data.source.local.entity.UserEntity

fun UserEntity.entityToResult(): ResultItem {
    return ResultItem(
        id = id,
        avatarUrl = avatar,
        login = username,
        htmlUrl = url
    )
}