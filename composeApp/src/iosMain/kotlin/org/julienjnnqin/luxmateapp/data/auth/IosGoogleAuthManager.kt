package org.julienjnnqin.luxmateapp.data.auth

import org.julienjnnqin.luxmateapp.domain.auth.SocialAuthManager

class IosGoogleAuthManager : SocialAuthManager {
    override suspend fun getGoogleIdToken(): String? = null // À implémenter plus tard avec ASWebAuthenticationSession
}