package org.julienjnnqin.luxmateapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val role: String = "USER",  // USER or ADMIN
    val createdAt: String
)

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val createdAt: String
)
