package com.example.model.user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(

    @BsonId
    val username : String,
    val name : String,
    @BsonId
    val email : String ,
    val password : String,
    val salt : String,
    @BsonId
    val id : ObjectId = ObjectId()
)
