package com.example.model.carPool

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class UserLocation (
    @BsonId
    val username: String,
    val latitude : String,
    val longitude : String
)