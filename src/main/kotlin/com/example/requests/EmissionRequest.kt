package com.example.requests

import kotlinx.serialization.Serializable

@Serializable
data class EmissionRequest (
    val username : String
)