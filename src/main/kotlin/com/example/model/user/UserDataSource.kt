package com.example.model.user

interface UserDataSource {
    suspend fun getUserByUsername(username : String) : User?

    suspend fun insertUser(user : User) : Boolean

}