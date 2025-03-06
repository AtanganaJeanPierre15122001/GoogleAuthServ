package com.example.repository

import com.example.model.User
import com.example.model.theUser
import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase

class UserRepository(private val database: CoroutineDatabase) {
    private val users = database.getCollection<theUser>()

    suspend fun getAllUsers(): List<theUser> = users.find().toList()

    suspend fun getUserById(id: String): theUser? = users.findOne(Filters.eq("_id", ObjectId(id)))

    suspend fun createUser(user: theUser): Boolean = users.insertOne(user).wasAcknowledged()

    suspend fun updateUser(id: String, user: theUser): Boolean {
        return users.replaceOne(Filters.eq("_id", ObjectId(id)), user).wasAcknowledged()
    }

    suspend fun deleteUser(id: String): Boolean = users.deleteOne(Filters.eq("_id", ObjectId(id))).wasAcknowledged()
}
