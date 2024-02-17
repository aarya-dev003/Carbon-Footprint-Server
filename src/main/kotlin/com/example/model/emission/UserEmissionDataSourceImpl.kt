package com.example.model.emission

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserEmissionDataSourceImpl(db : CoroutineDatabase) : UserEmissionDataSource {
    private val users = db.getCollection<UserEmission>()

    override suspend fun getUserByUsername(id: String): UserEmission? {
        return users.findOne(UserEmission::username eq id)
    }

    override suspend fun insertUserEmission(user: UserEmission): Boolean {
        val userExists: Boolean = users.findOneById(user.username) != null
        return if (userExists) {
            users.updateOneById(user.username, user).wasAcknowledged()
        } else {
            users.insertOne(user).wasAcknowledged()
        }
    }

    override suspend fun getAllUser(): List<UserEmission> {
        val user = users.find().toList()
        return user
    }
}