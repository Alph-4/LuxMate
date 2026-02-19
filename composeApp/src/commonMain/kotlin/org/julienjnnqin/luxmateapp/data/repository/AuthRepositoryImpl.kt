package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Mock implementation - in production, call API
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(
                    id = "user_001",
                    name = "John Doe",
                    email = email,
                    avatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuDiZpEiT1zOZFC4-sujixHU56w6BENvi8XHTHFp9B9sSQiC0QYOV7OWb9jw_r2IH2hN2ZALYXxg82F9_dLWUh-Oo8q_SqGlT4wL-Uh07m8jOB7Af3Gcv1sxMhMAW3bBt9tiIFSM3pL2-0C_TQr6-kn4fyWdcNjRlzehwaoTLZnjCXnh7viAecOwg25O604vpvZy2r04SP373a15FLBEvnxpSPtDlEtRYIrUfm3st_RxM1x0Reb9WOG4bCSR0JQfAIK2GfhOwuw56r3x"
                )
                currentUser = user
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            currentUser = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            Result.success(currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

