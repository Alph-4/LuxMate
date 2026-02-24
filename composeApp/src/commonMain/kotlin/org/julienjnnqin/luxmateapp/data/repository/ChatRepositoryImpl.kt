package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.model.ChatSession
import org.julienjnnqin.luxmateapp.data.model.ChatMessage
import org.julienjnnqin.luxmateapp.data.model.SendMessageRequest
import org.julienjnnqin.luxmateapp.data.model.SendMessageResponse
import org.julienjnnqin.luxmateapp.data.remote.backendApi
import org.julienjnnqin.luxmateapp.domain.repository.ChatRepository


class ChatRepositoryImpl(
    private val backendApi: backendApi,
) : ChatRepository {
    // Get user sessions
    suspend fun getSessions(): List<ChatSession> {
        return backendApi.getSessions()
    }

    // Create new session
    suspend fun createSession(personaId: String): ChatSession {
        return backendApi.createSession(personaId)
    }

    // Get session details
    suspend fun getSession(sessionId: String): ChatSession {
        return backendApi.getSession(sessionId)
    }

    // Get messages from session
    suspend fun getMessages(sessionId: String): List<ChatMessage> {
        return backendApi.getMessages(sessionId)
    }

    // Send message and get response
    suspend fun sendMessage(sessionId: String, content: String): SendMessageResponse {
        return backendApi.sendMessage(sessionId, SendMessageRequest(content))
    }
}
