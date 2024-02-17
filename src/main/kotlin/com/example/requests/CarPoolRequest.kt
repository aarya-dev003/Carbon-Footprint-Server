package com.example.requests

import kotlinx.serialization.Serializable

@Serializable
data class CarPoolRequest(

    val username : String,
    val latitude : String,
    val longitude : String
)