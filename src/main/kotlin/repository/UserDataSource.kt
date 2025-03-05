package com.example.repository

import com.example.model.User

interface UserDataSource {

    suspend fun getUserInfo(userId : String): User?

    suspend fun saveUserInfo(user: User) : Boolean

    suspend fun deleteUser(userId : String): Boolean

    suspend fun updateUserInfo(
        userId: String,
        firstname: String,
        lastname: String,
    ):Boolean
}