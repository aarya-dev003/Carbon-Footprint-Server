package com.example.model.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSourceImpl(db : CoroutineDatabase) : UserDataSource {
    private val users = db.getCollection<User>()
    override suspend fun getUserByUsername(username: String): User? {
        return users.findOne(User :: username eq username)
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

}