package com.example.models

import kotlinx.serialization.Serializable


@Serializable

data class ApiResponse(
    val success : Boolean
)
