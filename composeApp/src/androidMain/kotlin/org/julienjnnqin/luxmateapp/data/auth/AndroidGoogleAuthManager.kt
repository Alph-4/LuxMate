package org.julienjnnqin.luxmateapp.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import org.julienjnnqin.luxmateapp.domain.auth.SocialAuthManager

class GoogleAuthManager(private val context: Context) : SocialAuthManager {
    private val credentialManager = CredentialManager.create(context)

    override suspend fun getGoogleIdToken(): String? {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            // ATTENTION : Utilise ici ton WEB CLIENT ID (celui de ton backend)
            .setServerClientId("202071320891-ojkgbokb6e3045atfjanr6le6c6cpkic.apps.googleusercontent.com")
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                googleIdTokenCredential.idToken
            } else null
        } catch (e: Exception) {
            println("❌ Google Auth Error: ${e.message}")
            null
        }
    }
}