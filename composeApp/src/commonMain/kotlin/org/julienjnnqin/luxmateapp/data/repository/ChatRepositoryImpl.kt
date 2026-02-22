package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.model.ChatSession
import org.julienjnnqin.luxmateapp.data.remote.KtorbackendApi


class ChatRepositoryImpl(
    private val backendApi: KtorbackendApi,

) {
    // Get user sessions
    suspend fun getSessions(): List<ChatSession> {
        return backendApi.getSessions()
        return httpClient.get("$baseUrl/chat/sessions").body()
    }

    // Create new session
    suspend fun createSession(personaId: String): ChatSession {
        return httpClient.post("$baseUrl/chat/sessions") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("personaId" to personaId))
        }.body()
    }

    // Get session details
    suspend fun getSession(sessionId: String): ChatSession {
        return httpClient.get("$baseUrl/chat/sessions/$sessionId").body()
    }

    // Get messages from session
    suspend fun getMessages(sessionId: String): List<ChatMessage> {
        return httpClient.get("$baseUrl/chat/sessions/$sessionId/messages").body()
    }

    // Send message and get response
    suspend fun sendMessage(sessionId: String, content: String): SendMessageResponse {
        return httpClient.post("$baseUrl/chat/sessions/$sessionId/messages") {
            contentType(ContentType.Application.Json)
            setBody(SendMessageRequest(content))
        }.body()
    }
}