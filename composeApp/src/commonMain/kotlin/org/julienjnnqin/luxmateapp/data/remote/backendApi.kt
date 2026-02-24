package org.julienjnnqin.luxmateapp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.CancellationException
import org.julienjnnqin.luxmateapp.data.auth.TokenStore
import org.julienjnnqin.luxmateapp.data.model.ChatMessage
import org.julienjnnqin.luxmateapp.data.model.ChatSession
import org.julienjnnqin.luxmateapp.data.model.CreateChatSessionRequest
import org.julienjnnqin.luxmateapp.data.model.LoginRequest
import org.julienjnnqin.luxmateapp.data.model.Persona
import org.julienjnnqin.luxmateapp.data.model.RefreshTokenRequest
import org.julienjnnqin.luxmateapp.data.model.SendMessageRequest
import org.julienjnnqin.luxmateapp.data.model.SendMessageResponse
import org.julienjnnqin.luxmateapp.data.model.TokenResponse

interface backendApi {
    // AUTH
    suspend fun login(email: String, password: String): TokenResponse
    suspend fun refreshToken(refreshToken: String): TokenResponse
    suspend fun getCurrentUser(): org.julienjnnqin.luxmateapp.data.model.UserResponse

    // PERSONAS
    suspend fun getPersonas(): List<Persona>
    suspend fun getPersonaById(personaId: String): Persona

    // CHAT SESSIONS
    suspend fun getSessions(): List<ChatSession>
    suspend fun createSession(personaId: String): ChatSession
    suspend fun getSession(sessionId: String): ChatSession

    // MESSAGES
    suspend fun getMessages(sessionId: String): List<ChatMessage>
    suspend fun sendMessage(sessionId: String, request: SendMessageRequest): SendMessageResponse
}

class KtorbackendApi(private val client: HttpClient, private val tokenStore: TokenStore) :
        backendApi {
    companion object {
        private const val API_URL = "https://luxmate.up.railway.app"
    }

    private suspend fun tryRefresh(): TokenResponse? {
        val refresh = tokenStore.getRefreshToken() ?: return null
        return try {
            client
                    .post("$API_URL/auth/refresh") {
                        header("Content-Type", ContentType.Application.Json.contentType)
                        setBody(RefreshTokenRequest(refresh))
                        // do not attach Authorization header here
                    }
                    .body()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun login(email: String, password: String): TokenResponse {
        return client
                .post("$API_URL/auth/login") {
                    header("Content-Type", ContentType.Application.Json.contentType)
                    setBody(LoginRequest(email, password))
                }
                .body()
    }

    override suspend fun refreshToken(refreshToken: String): TokenResponse {
        return client
                .post("$API_URL/auth/refresh") {
                    header("Content-Type", ContentType.Application.Json.contentType)
                    setBody(RefreshTokenRequest(refreshToken))
                }
                .body()
    }

    override suspend fun getPersonas(): List<Persona> {
        try {
            val token = tokenStore.getAccessToken()
            val resp: HttpResponse =
                    client.get("$API_URL/personas") {
                        if (!token.isNullOrEmpty()) header("Authorization", "Bearer $token")
                        this
                    }
            if (resp.status == HttpStatusCode.Unauthorized) {
                val newTokens = tryRefresh() ?: return emptyList()
                tokenStore.save(newTokens)
                return client
                        .get("$API_URL/personas") {
                            header("Authorization", "Bearer ${newTokens.accessToken}")
                        }
                        .body()
            }
            return resp.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return emptyList()
        }
    }

    override suspend fun getPersonaById(personaId: String): Persona {
        try {
            val token = tokenStore.getAccessToken()
            val resp: HttpResponse =
                    client.get("$API_URL/personas/$personaId") {
                        if (!token.isNullOrEmpty()) header("Authorization", "Bearer $token")
                    }
            if (resp.status == HttpStatusCode.Unauthorized) {
                val newTokens = tryRefresh() ?: throw Exception("Unauthorized")
                tokenStore.save(newTokens)
                return client
                        .get("$API_URL/personas/$personaId") {
                            header("Authorization", "Bearer ${newTokens.accessToken}")
                        }
                        .body()
            }
            return resp.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getSessions(): List<ChatSession> {
        try {
            val token = tokenStore.getAccessToken()
            val resp: HttpResponse =
                    client.get("$API_URL/chat/sessions") {
                        if (!token.isNullOrEmpty()) header("Authorization", "Bearer $token")
                    }
            if (resp.status == HttpStatusCode.Unauthorized) {
                val newTokens = tryRefresh() ?: return emptyList()
                tokenStore.save(newTokens)
                return client
                        .get("$API_URL/chat/sessions") {
                            header("Authorization", "Bearer ${newTokens.accessToken}")
                        }
                        .body()
            }
            return resp.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return emptyList()
        }
    }

    override suspend fun createSession(personaId: String): ChatSession {
        try {
            val token = tokenStore.getAccessToken()
            val resp: HttpResponse =
                    client.post("$API_URL/chat/sessions") {
                        if (!token.isNullOrEmpty()) header("Authorization", "Bearer $token")
                        header("Content-Type", ContentType.Application.Json.contentType)
                        setBody(CreateChatSessionRequest(personaId))
                    }
            if (resp.status == HttpStatusCode.Unauthorized) {
                val newTokens = tryRefresh() ?: throw Exception("Unauthorized")
                tokenStore.save(newTokens)
                return client
                        .post("$API_URL/chat/sessions") {
                            header("Authorization", "Bearer ${newTokens.accessToken}")
                            header("Content-Type", ContentType.Application.Json.contentType)
                            setBody(CreateChatSessionRequest(personaId))
                        }
                        .body()
            }
            return resp.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getSession(sessionId: String): ChatSession {
        try {
            val token = tokenStore.getAccessToken()
            val resp: HttpResponse =
                    client.get("$API_URL/chat/sessions/$sessionId") {
                        if (!token.isNullOrEmpty()) header("Authorization", "Bearer $token")
                    }
            if (resp.status == HttpStatusCode.Unauthorized) {
                val newTokens = tryRefresh() ?: throw Exception("Unauthorized")
                tokenStore.save(newTokens)
                return client
                        .get("$API_URL/chat/sessions/$sessionId") {
                            header("Authorization", "Bearer ${newTokens.accessToken}")
                        }
                        .body()
            }
            return resp.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getCurrentUser(): org.julienjnnqin.luxmateapp.data.model.UserResponse {
        try {
            val token = tokenStore.getAccessToken()
            val resp: HttpResponse =
                    client.get("$API_URL/user/me") {
                        if (!token.isNullOrEmpty()) header("Authorization", "Bearer $token")
                    }
            if (resp.status == HttpStatusCode.Unauthorized) {
                val newTokens = tryRefresh() ?: throw Exception("Unauthorized")
                tokenStore.save(newTokens)
                return client
                        .get("$API_URL/user/me") {
                            header("Authorization", "Bearer ${newTokens.accessToken}")
                        }
                        .body()
            }
            return resp.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getMessages(sessionId: String): List<ChatMessage> {
        try {
            val token = tokenStore.getAccessToken()
            val resp: HttpResponse =
                    client.get("$API_URL/chat/sessions/$sessionId/messages") {
                        if (!token.isNullOrEmpty()) header("Authorization", "Bearer $token")
                    }
            if (resp.status == HttpStatusCode.Unauthorized) {
                val newTokens = tryRefresh() ?: return emptyList()
                tokenStore.save(newTokens)
                return client
                        .get("$API_URL/chat/sessions/$sessionId/messages") {
                            header("Authorization", "Bearer ${newTokens.accessToken}")
                        }
                        .body()
            }
            return resp.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return emptyList()
        }
    }

    override suspend fun sendMessage(
            sessionId: String,
            request: SendMessageRequest
    ): SendMessageResponse {
        try {
            val token = tokenStore.getAccessToken()
            val resp: HttpResponse =
                    client.post("$API_URL/chat/sessions/$sessionId/messages") {
                        if (!token.isNullOrEmpty()) header("Authorization", "Bearer $token")
                        header("Content-Type", ContentType.Application.Json.contentType)
                        setBody(request)
                    }
            if (resp.status == HttpStatusCode.Unauthorized) {
                val newTokens = tryRefresh() ?: throw Exception("Unauthorized")
                tokenStore.save(newTokens)
                return client
                        .post("$API_URL/chat/sessions/$sessionId/messages") {
                            header("Authorization", "Bearer ${newTokens.accessToken}")
                            header("Content-Type", ContentType.Application.Json.contentType)
                            setBody(request)
                        }
                        .body()
            }
            return resp.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            throw e
        }
    }
}
