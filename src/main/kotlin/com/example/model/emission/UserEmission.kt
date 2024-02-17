package com.example.model.emission

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class UserEmission(
    @BsonId
    val username : String ,
    val emission : String
)
