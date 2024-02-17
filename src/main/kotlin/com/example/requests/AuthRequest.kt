package com.example.requests

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class AuthRequest(
    @BsonId
    val username  : String ,
    @BsonId
    val email : String ,
    val password : String,
    val name : String
)
