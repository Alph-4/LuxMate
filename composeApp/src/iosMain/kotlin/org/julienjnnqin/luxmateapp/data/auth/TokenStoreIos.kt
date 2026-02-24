package org.julienjnnqin.luxmateapp.data.auth

import org.julienjnnqin.luxmateapp.data.model.TokenResponse
import platform.Foundation.NSUserDefaults

/**
 * Simple iOS TokenStore using NSUserDefaults. For production use, prefer storing tokens in the
 * Keychain.
 */
class TokenStoreIos : TokenStore {
    private val defaults = NSUserDefaults.standardUserDefaults()

    private companion object {
        const val KEY_ACCESS = "luxmate_access_token"
        const val KEY_REFRESH = "luxmate_refresh_token"
    }

    override suspend fun getAccessToken(): String? = defaults.stringForKey(KEY_ACCESS)

    override suspend fun getRefreshToken(): String? = defaults.stringForKey(KEY_REFRESH)

    override suspend fun save(tokenResponse: TokenResponse) {
        defaults.setObject(tokenResponse.accessToken, KEY_ACCESS)
        defaults.setObject(tokenResponse.refreshToken, KEY_REFRESH)
        defaults.synchronize()
    }

    override suspend fun clear() {
        defaults.removeObjectForKey(KEY_ACCESS)
        defaults.removeObjectForKey(KEY_REFRESH)
        defaults.synchronize()
    }
}
