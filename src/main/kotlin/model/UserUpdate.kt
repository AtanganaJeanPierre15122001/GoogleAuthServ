package com.example.model

import kotlinx.serialization.Serializable


@Serializable
data class UserUpdate(
    val firstName : String,
    val lastName : String
)
