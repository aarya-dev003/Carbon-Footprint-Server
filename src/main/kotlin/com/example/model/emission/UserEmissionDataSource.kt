package com.example.model.emission

interface UserEmissionDataSource {
    suspend fun getUserByUsername(id: String): UserEmission?
    suspend fun insertUserEmission(user: UserEmission): Boolean
    suspend fun getAllUser(): List<UserEmission>
}