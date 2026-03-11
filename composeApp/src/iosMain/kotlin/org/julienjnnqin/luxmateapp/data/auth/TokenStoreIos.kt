package org.julienjnnqin.luxmateapp.data.auth

/**
 * Simple iOS TokenStore using NSUserDefaults. For production use, prefer storing tokens in the
 * Keychain.
 */
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.julienjnnqin.luxmateapp.data.model.TokenResponse
import org.julienjnnqin.luxmateapp.domain.repository.SettingsRepository
import platform.Foundation.NSUserDefaults

class TokenStoreIos : SettingsRepository {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    // 1. Initialisation du StateFlow avec la valeur actuelle en cache
    private val _isLoggedIn = MutableStateFlow(userDefaults.stringForKey(KEY_ACCESS) != null)
    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentLanguage = MutableStateFlow("en") // Valeur par défaut
    override val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private companion object {
        const val KEY_ACCESS = "luxmate_access_token"
        const val KEY_REFRESH = "luxmate_refresh_token"
        const val KEY_LANGUAGE = "luxmate_language"
    }

    override suspend fun getAccessToken(): String? = userDefaults.stringForKey(KEY_ACCESS)

    override suspend fun getRefreshToken(): String? = userDefaults.stringForKey(KEY_REFRESH)

    override suspend fun saveUserToken(tokenResponse: TokenResponse) {
        userDefaults.setObject(tokenResponse.accessToken, forKey = KEY_ACCESS)
        userDefaults.setObject(tokenResponse.refreshToken, forKey = KEY_REFRESH)

        // 2. On notifie le changement
        _isLoggedIn.value = true
    }

    override suspend fun cleaUserToken() {
        userDefaults.removeObjectForKey(KEY_ACCESS)
        userDefaults.removeObjectForKey(KEY_REFRESH)

        // 3. On notifie la déconnexion
        _isLoggedIn.value = false
    }

    override suspend fun setLanguage(language: String) {
        userDefaults.setObject(language, forKey = KEY_LANGUAGE)
        _currentLanguage.value = language
    }

    override suspend fun getLanguage(): String {
        _currentLanguage.value = userDefaults.stringForKey(KEY_LANGUAGE) ?: "en"
        return _currentLanguage.value
    }
}