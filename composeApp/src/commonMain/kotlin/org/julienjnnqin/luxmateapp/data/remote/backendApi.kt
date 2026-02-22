package org.julienjnnqin.luxmateapp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.utils.io.CancellationException
import org.julienjnnqin.luxmateapp.data.model.ChatSession
import org.julienjnnqin.luxmateapp.data.model.ChatMessage
import org.julienjnnqin.luxmateapp.data.model.SendMessageRequest
import org.julienjnnqin.luxmateapp.data.model.SendMessageResponse
import org.julienjnnqin.luxmateapp.data.model.CreateChatSessionRequest

interface backendApi {
    //PERSONAS
    suspend fun getPersonas(): List<Unit>
    suspend fun getPersonaById(personaId: String)

    //CHAT SESSIONS
    suspend fun getSessions(): List<ChatSession>
    suspend fun createSession(personaId: String): ChatSession
    suspend fun getSession(sessionId: String): ChatSession

    //MESSAGES
    suspend fun getMessages(sessionId: String): List<ChatMessage>
    suspend fun sendMessage(sessionId: String, request: SendMessageRequest): SendMessageResponse


    //USER PROFILES
}

class KtorbackendApi(private val client: HttpClient) : backendApi {
    companion object {
        private const val API_URL = ""
    }

    override suspend fun getPersonas(): List<Unit> {
        return try {
            client.get(API_URL).body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPersonaById(personaId: String) {
        try {
            client.get("$API_URL/personas/$personaId")
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
        }
    }

    override suspend fun getSessions(): List<ChatSession> {
        return try {
            client.get("$API_URL/chat/sessions").body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun createSession(personaId: String): ChatSession {
        return try {
            client.post("$API_URL/chat/sessions") {
                header("Content-Type", ContentType.Application.Json.contentType)
                setBody(CreateChatSessionRequest(personaId))
            }.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getSession(sessionId: String): ChatSession {
        return try {
            client.get("$API_URL/chat/sessions/$sessionId").body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getMessages(sessionId: String): List<ChatMessage> {
        return try {
            client.get("$API_URL/chat/sessions/$sessionId/messages").body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun sendMessage(sessionId: String, request: SendMessageRequest): SendMessageResponse {
        return try {
            client.post("$API_URL/chat/sessions/$sessionId/messages") {
                header("Content-Type", ContentType.Application.Json.contentType)
                setBody(request)
            }.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            throw e
        }
    }
}

