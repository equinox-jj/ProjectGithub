package com.projectgithub.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.projectgithub.data.source.local.dao.UserDao
import com.projectgithub.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}