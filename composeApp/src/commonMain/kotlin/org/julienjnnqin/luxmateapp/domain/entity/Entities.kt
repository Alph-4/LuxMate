package org.julienjnnqin.luxmateapp.domain.entity

import kotlinx.serialization.Serializable
import org.julienjnnqin.luxmateapp.core.utils.TeacherLevel
import org.julienjnnqin.luxmateapp.core.utils.Role
import org.julienjnnqin.luxmateapp.core.utils.TeacherTheme

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatar: String? = null,
    val role: Role = Role.USER
)

@Serializable
data class Persona(
    val id: String,
    val name: String,
    val subject: String,
    val theme: TeacherTheme,
    val level: TeacherLevel,
    val description: String,
    val avatar: String,
    val rating: Float = 0f
)

@Serializable
data class OnboardingState(
    val isCompleted: Boolean = false,
    val currentPage: Int = 0
)

@Serializable
data class ChatHistory(
    val id: String,
    val teacherId: String,
    val teacherName: String,
    val subject: String,
    val date: String,
    val lastMessage: String
)

