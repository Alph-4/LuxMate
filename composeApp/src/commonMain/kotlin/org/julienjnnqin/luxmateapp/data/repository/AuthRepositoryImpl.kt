package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.auth.TokenStore
import org.julienjnnqin.luxmateapp.data.remote.backendApi
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: backendApi,
    private val tokenStore: TokenStore
) : AuthRepository {
    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val tokenResponse = api.login(email, password)
            tokenStore.save(tokenResponse)
            val user = User(
                id = tokenResponse.userId,
                name = "",
                email = ""
            )
            currentUser = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            tokenStore.clear()
            currentUser = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            // Try fetch from API to have latest profile
            val resp = api.getCurrentUser()
            val user = User(
                id = resp.id,
                name = resp.email.substringBefore('@'),
                email = resp.email,
                avatar = null
            )
            currentUser = user
            Result.success(user)
        } catch (e: Exception) {
            // Fall back to cached user if available
            if (currentUser != null) Result.success(currentUser) else Result.failure(e)
        }
    }
}

