package org.julienjnnqin.luxmateapp.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.julienjnnqin.luxmateapp.data.model.TokenResponse
import org.julienjnnqin.luxmateapp.domain.repository.SettingsRepository


class SettingsRepositoryImpl(private val settings: Settings) : SettingsRepository {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    private val _isLoggedIn = MutableStateFlow(settings.hasKey("access_token"))
    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    override suspend fun getAccessToken(): String? = settings.getStringOrNull(KEY_ACCESS_TOKEN)

    override suspend fun getRefreshToken(): String? = settings.getStringOrNull(KEY_REFRESH_TOKEN)

    override suspend fun saveUserToken(tokenResponse: TokenResponse) {
        settings[KEY_ACCESS_TOKEN] = tokenResponse.accessToken
        settings[KEY_REFRESH_TOKEN] = tokenResponse.refreshToken
        _isLoggedIn.value = true
    }

    override suspend fun cleaUserToken() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        _isLoggedIn.value = false
    }
}