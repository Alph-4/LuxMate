package org.julienjnnqin.luxmateapp.domain.auth

interface SocialAuthManager {
    suspend fun getGoogleIdToken(): String?
}