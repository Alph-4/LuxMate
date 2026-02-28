package org.julienjnnqin.luxmateapp.domain.repository

import org.julienjnnqin.luxmateapp.domain.entity.OnboardingState
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.entity.Persona
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory

interface OnboardingRepository {
    suspend fun getOnboardingState(): OnboardingState
    suspend fun setOnboardingCompleted(): Result<Unit>
}

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): Result<User?>
}

interface TeacherRepository {
    suspend fun getAllTeachers(): Result<List<Persona>>
    suspend fun getTeacherById(id: String): Result<Persona>
    suspend fun searchTeachers(query: String): Result<List<Persona>>
}

interface UserRepository {
    suspend fun getUserProfile(): Result<User>
    suspend fun getChatHistory(): Result<List<ChatHistory>>
    suspend fun updateUserProfile(user: User): Result<User>
}

