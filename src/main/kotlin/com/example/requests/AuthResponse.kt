package com.example.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
        val token : String
)
