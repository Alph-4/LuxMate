package org.julienjnnqin.luxmateapp.data.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.julienjnnqin.luxmateapp.data.model.TokenResponse

interface TokenStore {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun save(tokenResponse: TokenResponse)
    suspend fun clear()
}

// Simple in-memory implementation used as a default. Replace with platform-secure storage later.
class InMemoryTokenStore : TokenStore {
    private val mutex = Mutex()
    private var access: String? = null
    private var refresh: String? = null

    override suspend fun getAccessToken(): String? = mutex.withLock { access }

    override suspend fun getRefreshToken(): String? = mutex.withLock { refresh }

    override suspend fun save(tokenResponse: TokenResponse) = mutex.withLock {
        access = tokenResponse.accessToken
        refresh = tokenResponse.refreshToken
    }

    override suspend fun clear() = mutex.withLock {
        access = null
        refresh = null
    }
}
