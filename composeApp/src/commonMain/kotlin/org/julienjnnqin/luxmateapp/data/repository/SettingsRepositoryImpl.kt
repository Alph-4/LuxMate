package org.julienjnnqin.luxmateapp.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import org.julienjnnqin.luxmateapp.data.model.TokenResponse
import org.julienjnnqin.luxmateapp.domain.repository.SettingsRepository


class SettingsRepositoryImpl(private val settings: Settings) : SettingsRepository {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    override suspend fun getAccessToken(): String? = settings.getStringOrNull(KEY_ACCESS_TOKEN)

    override suspend fun getRefreshToken(): String? = settings.getStringOrNull(KEY_REFRESH_TOKEN)

    override suspend fun saveUserToken(tokenResponse: TokenResponse) {
        settings[KEY_ACCESS_TOKEN] = tokenResponse.accessToken
        settings[KEY_REFRESH_TOKEN] = tokenResponse.refreshToken
    }

    override suspend fun cleaUserToken() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
    }
}