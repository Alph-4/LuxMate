package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.remote.BackendApi
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.repository.UserRepository

class UserRepositoryImpl(private val api: BackendApi) : UserRepository {
    override suspend fun getUserProfile(): Result<User> {
        return try {
            val resp = api.getCurrentUser()
            val user =
                User(
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
            val sessions = api.getSessions()

            Result.success(sessions.map { it.toDomain() })
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
