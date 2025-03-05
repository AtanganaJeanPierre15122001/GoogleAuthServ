package com.example.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id : String,
    val name : String,
    val emailAdress : String,
    val profilePhoto : String
)
