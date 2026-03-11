package org.julienjnnqin.luxmateapp.data.auth

/**
 * Simple Android TokenStore using SharedPreferences. Replace with EncryptedSharedPreferences or
 * DataStore for production. Usage: provide this implementation in your Android application Koin
 * initialization.
 */
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.julienjnnqin.luxmateapp.data.model.TokenResponse
import org.julienjnnqin.luxmateapp.domain.repository.SettingsRepository

class TokenStoreAndroid(
    context: Context,
) : SettingsRepository {
    private val prefs = context.getSharedPreferences("luxmate_tokens", Context.MODE_PRIVATE)

    // 1. On crée le StateFlow initialisé avec la présence ou non d'un token
    private val _isLoggedIn = MutableStateFlow(prefs.contains(KEY_ACCESS))
    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentLanguage = MutableStateFlow("en") // Valeur par défaut
    override val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private companion object {
        const val KEY_ACCESS = "luxmate_access_token"
        const val KEY_REFRESH = "luxmate_refresh_token"
        const val KEY_LANGUAGE = "luxmate_language"
    }

    override suspend fun getAccessToken(): String? = prefs.getString(KEY_ACCESS, null)

    override suspend fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH, null)

    override suspend fun saveUserToken(tokenResponse: TokenResponse) {
        prefs.edit()
            .putString(KEY_ACCESS, tokenResponse.accessToken)
            .putString(KEY_REFRESH, tokenResponse.refreshToken)
            .apply()

        // 2. On notifie les abonnés (comme ton AppViewModel)
        _isLoggedIn.value = true
    }

    override suspend fun cleaUserToken() {
        prefs.edit().remove(KEY_ACCESS).remove(KEY_REFRESH).apply()

        // 3. On notifie la déconnexion
        _isLoggedIn.value = false
    }

    override suspend fun setLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
        _currentLanguage.value = language
    }

    override suspend fun getLanguage(): String {
        _currentLanguage.value = prefs.getString(KEY_LANGUAGE, "en") ?: "en"
        return _currentLanguage.value
    }
}