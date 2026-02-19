package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory
import org.julienjnnqin.luxmateapp.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    override suspend fun getUserProfile(): Result<User> {
        return try {
            val user = User(
                id = "user_001",
                name = "John Doe",
                email = "john.doe@university.com",
                avatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuDiZpEiT1zOZFC4-sujixHU56w6BENvi8XHTHFp9B9sSQiC0QYOV7OWb9jw_r2IH2hN2ZALYXxg82F9_dLWUh-Oo8q_SqGlT4wL-Uh07m8jOB7Af3Gcv1sxMhMAW3bBt9tiIFSM3pL2-0C_TQr6-kn4fyWdcNjRlzehwaoTLZnjCXnh7viAecOwg25O604vpvZy2r04SP373a15FLBEvnxpSPtDlEtRYIrUfm3st_RxM1x0Reb9WOG4bCSR0JQfAIK2GfhOwuw56r3x"
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChatHistory(): Result<List<ChatHistory>> {
        return try {
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
            // Mock implementation
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

