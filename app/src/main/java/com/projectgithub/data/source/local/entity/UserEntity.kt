package com.projectgithub.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.projectgithub.common.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val avatar: String,
    val username: String,
    val url: String,
)
