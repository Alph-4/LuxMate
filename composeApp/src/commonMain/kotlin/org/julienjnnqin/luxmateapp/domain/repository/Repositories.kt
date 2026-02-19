package org.julienjnnqin.luxmateapp.domain.repository

import org.julienjnnqin.luxmateapp.domain.entity.OnboardingState
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.entity.Teacher
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory

interface OnboardingRepository {
    suspend fun getOnboardingState(): OnboardingState
    suspend fun setOnboardingCompleted()
}

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): Result<User?>
}

interface TeacherRepository {
    suspend fun getAllTeachers(): Result<List<Teacher>>
    suspend fun getTeacherById(id: String): Result<Teacher>
    suspend fun searchTeachers(query: String): Result<List<Teacher>>
}

interface UserRepository {
    suspend fun getUserProfile(): Result<User>
    suspend fun getChatHistory(): Result<List<ChatHistory>>
    suspend fun updateUserProfile(user: User): Result<User>
}

