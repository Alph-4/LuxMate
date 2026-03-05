package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.remote.BackendApi
import org.julienjnnqin.luxmateapp.domain.auth.SocialAuthManager
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.repository.AuthRepository
import org.julienjnnqin.luxmateapp.domain.repository.SettingsRepository


class AuthRepositoryImpl(
    private val api: BackendApi,
    private val settings: SettingsRepository,
    private val socialAuth: SocialAuthManager
) :
    AuthRepository {

    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val tokenResponse = api.login(email, password)
            settings.saveUserToken(tokenResponse)
            val user = User(id = tokenResponse.userId, name = "", email = "")
            currentUser = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            settings.cleaUserToken()
            currentUser = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun googleSignIn(): Result<User> {
        return try {
            // 1. Déclencher l'UI système (Android/iOS) pour récupérer l'idToken
            val idToken = socialAuth.getGoogleIdToken()
                ?: return Result.failure(Exception("Google Sign-In annulé par l'utilisateur"))

            // 2. Envoyer cet idToken à ton backend Railway
            val tokenResponse = api.googleSignIn(idToken)

            // 3. Sauvegarder les tokens (Access + Refresh) via ton settings repo
            settings.saveUserToken(tokenResponse)

            // 4. Créer l'objet User pour ton domaine
            val user = User(
                id = tokenResponse.userId,
                name = "", // On pourra le peupler via getCurrentUser() après
                email = ""
            )
            currentUser = user
            Result.success(user)
        } catch (e: Exception) {
            println("❌ Error in AuthRepository.loginWithGoogle: ${e.message}")
            Result.failure(e)
        }
    }


    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            // Try fetch from API to have latest profile
            val resp = api.getCurrentUser()
            val user =
                User(
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
