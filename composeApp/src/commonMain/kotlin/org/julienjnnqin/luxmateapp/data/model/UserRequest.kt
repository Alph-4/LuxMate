package org.julienjnnqin.luxmateapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val role: String = "USER", // USER or ADMIN
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("google_id")
    val googleId: String? = null,
)

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("google_id")
    val googleId: String? = null,
    val role: String = "USER"
)
