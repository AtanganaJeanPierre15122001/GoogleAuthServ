package com.example.models

import com.example.model.User
import kotlinx.serialization.Serializable


@Serializable

data class ApiResponse(
    val success : Boolean,
    val user : User? = null,
    val message : String? = null
)
