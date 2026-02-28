package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.model.ChatMessage
import org.julienjnnqin.luxmateapp.data.model.ChatSession
import org.julienjnnqin.luxmateapp.data.model.SendMessageRequest
import org.julienjnnqin.luxmateapp.data.model.SendMessageResponse
import org.julienjnnqin.luxmateapp.data.remote.BackendApi
import org.julienjnnqin.luxmateapp.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val backendApi: BackendApi,
) : ChatRepository {
    // Get user sessions
    override suspend fun getSessions(): List<ChatSession> {
        return backendApi.getSessions()
    }

    // Create new session
    override suspend fun createSession(personaId: String): ChatSession {
        return backendApi.createSession(personaId)
    }

    // Get session details
    override suspend fun getSession(sessionId: String): ChatSession {
        return backendApi.getSession(sessionId)
    }

    // Get messages from session
    override suspend fun getMessages(sessionId: String): List<ChatMessage> {
        return backendApi.getMessages(sessionId)
    }

    // Send message and get response
    override suspend fun sendMessage(sessionId: String, request: SendMessageRequest): SendMessageResponse {
        return backendApi.sendMessage(sessionId, request)
    }
}
