package com.example.model.carPool

interface LocationDataSource {
    suspend fun getUserByUsername(id: String): UserLocation?
    suspend fun insertUserEmission(user: UserLocation): Boolean
    suspend fun getAllUser(): List<UserLocation>
}