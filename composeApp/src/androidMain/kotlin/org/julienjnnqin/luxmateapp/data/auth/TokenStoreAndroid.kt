package org.julienjnnqin.luxmateapp.data.auth

import android.content.Context
import org.julienjnnqin.luxmateapp.data.model.TokenResponse

/**
 * Simple Android TokenStore using SharedPreferences.
 * Replace with EncryptedSharedPreferences or DataStore for production.
 * Usage: provide this implementation in your Android application Koin initialization.
 */
class TokenStoreAndroid(context: Context) : TokenStore {
    private val prefs = context.getSharedPreferences("luxmate_tokens", Context.MODE_PRIVATE)

    private companion object {
        const val KEY_ACCESS = "luxmate_access_token"
        const val KEY_REFRESH = "luxmate_refresh_token"
    }

    override suspend fun getAccessToken(): String? = prefs.getString(KEY_ACCESS, null)

    override suspend fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH, null)

    override suspend fun save(tokenResponse: TokenResponse) {
        prefs.edit()
            .putString(KEY_ACCESS, tokenResponse.accessToken)
            .putString(KEY_REFRESH, tokenResponse.refreshToken)
            .apply()
    }

    override suspend fun clear() {
        prefs.edit().remove(KEY_ACCESS).remove(KEY_REFRESH).apply()
    }
}
