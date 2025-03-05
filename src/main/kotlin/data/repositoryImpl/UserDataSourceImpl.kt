package com.example.data.repositoryImpl

import com.example.model.User
import com.example.repository.UserDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class UserDataSourceImpl(
    database : CoroutineDatabase
) : UserDataSource{

    private val users = database.getCollection<User>()

    override suspend fun getUserInfo(userId: String): User? {
        return users.findOne(filter = User::id eq userId)
    }

    override suspend fun saveUserInfo(user: User): Boolean {
        val existingUser = users.findOne(filter = User::id eq user.id)
        return if (existingUser == null){
            users.insertOne(document = user).wasAcknowledged()
        }
        else{
            true
        }
    }

    override suspend fun deleteUser(userId: String): Boolean {
       return users.deleteOne(filter = User::id eq userId).wasAcknowledged()
    }

    override suspend fun updateUserInfo(userId: String, firstname: String, lastname: String): Boolean {
        return users.updateOne(
            filter = User::id eq userId,
            update = setValue(
                property = User::name,
                value = "$firstname $lastname"
            )
        ).wasAcknowledged()
    }
}