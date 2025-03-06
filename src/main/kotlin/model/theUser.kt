package com.example.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class theUser(
    @BsonId val id: ObjectId = ObjectId(),
    val name: String,
    val email: String,
    val age: Int
)
