package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.remote.backendApi
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory
import org.julienjnnqin.luxmateapp.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: backendApi
) : UserRepository {
    override suspend fun getUserProfile(): Result<User> {
        return try {
            val resp = api.getCurrentUser()
            val user = User(
                id = resp.id,
                name = resp.email.substringBefore('@'),
                email = resp.email,
                avatar = null
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChatHistory(): Result<List<ChatHistory>> {
        return try {
            // Keep mock chat history for now
            val chatHistory = listOf(
                ChatHistory(
                    id = "chat_001",
                    teacherId = "teacher_001",
                    teacherName = "Prof. Jean Dupont",
                    subject = "Intelligence Artificielle",
                    date = "12 Oct 2023",
                    lastMessage = "Excellente question sur les réseaux de neurones..."
                ),
                ChatHistory(
                    id = "chat_002",
                    teacherId = "teacher_002",
                    teacherName = "Dr. Marie Curie",
                    subject = "Physique Fondamentale",
                    date = "10 Oct 2023",
                    lastMessage = "Je vais expliquer ce concept avec plus de détails..."
                ),
                ChatHistory(
                    id = "chat_003",
                    teacherId = "teacher_003",
                    teacherName = "Prof. Alan Turing",
                    subject = "Logique Mathématique",
                    date = "05 Oct 2023",
                    lastMessage = "Les preuves formelles sont fascinantes..."
                )
            )
            Result.success(chatHistory)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(user: User): Result<User> {
        return try {
            // TODO: call API to update profile
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

