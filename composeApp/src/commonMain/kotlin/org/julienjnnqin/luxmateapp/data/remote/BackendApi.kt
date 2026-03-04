package org.julienjnnqin.luxmateapp.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.julienjnnqin.luxmateapp.data.model.*

interface BackendApi {
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

class KtorbackendApi(private val client: HttpClient) :
    BackendApi {
    companion object {
        private const val API_URL = "https://luxmate.up.railway.app"
    }

    private fun isSuccess(status: HttpStatusCode): Boolean = status.value in 200..299


    override suspend fun login(email: String, password: String): TokenResponse {
        println("login: email=$email")
        val resp = client
            .post("${API_URL}/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
        if (isSuccess(resp.status)) {
            val body: TokenResponse = resp.body()
            println("login: success ${resp.bodyAsText()}")
            return body
        } else {
            val err = resp.bodyAsText()
            println("login: response status=${resp.status} , body=$err")
            throw Exception(err)
        }
    }

    override suspend fun refreshToken(refreshToken: String): TokenResponse {
        println("refreshToken: manual refresh")
        val resp = client
            .post("${API_URL}/auth/refresh") {
                header("Content-Type", ContentType.Application.Json.contentType)
                setBody(RefreshTokenRequest(refreshToken))
            }
        val body: TokenResponse = resp.body()
        println("refreshToken: success")
        return body
    }

    override suspend fun getPersonas(): List<Persona> = client.get("$API_URL/personas").body()

    override suspend fun getPersonaById(personaId: String): Persona =
        client.get("$API_URL/personas/$personaId").body()

    override suspend fun getSessions(): List<ChatSession> =
        client.get("$API_URL/chat/sessions").body()

    override suspend fun createSession(personaId: String): ChatSession =
        client.post("$API_URL/chat/sessions").body()

    override suspend fun getSession(sessionId: String): ChatSession =
        client.get("$API_URL/chat/sessions/$sessionId").body()

    override suspend fun getCurrentUser(): UserResponse =
        client.get("$API_URL/auth/me").body()

    override suspend fun getMessages(sessionId: String): List<ChatMessage> =
        client.get("$API_URL/chat/sessions/$sessionId/messages").body()

    override suspend fun sendMessage(
        sessionId: String,
        request: SendMessageRequest
    ): SendMessageResponse =
        client.post("$API_URL/chat/sessions/$sessionId/messages").body()
}
