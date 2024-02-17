package com.example.model.carPool

import com.example.model.emission.UserEmission
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class LocationDataSourceImpl (db : CoroutineDatabase) : LocationDataSource  {
    private val users = db.getCollection<UserLocation>()

    override suspend fun getUserByUsername(id: String): UserLocation? {
        return users.findOne(UserEmission::username eq id)
    }

    override suspend fun insertUserEmission(user: UserLocation): Boolean {
        val userExists: Boolean = users.findOneById(user.username) != null
        return if (userExists) {
            users.updateOneById(user.username, user).wasAcknowledged()
        } else {
            users.insertOne(user).wasAcknowledged()
        }
    }

    override suspend fun getAllUser(): List<UserLocation> {
        val user = users.find().toList()
        return user
    }
}