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
        println("ChatRepo creating session for personaId=$personaId")

        return try {
            val session = backendApi.createSession(personaId)
            println("✅ ChatRepo Session created successfully: ${session.id}")
            session // Retourne la session si tout va bien
        } catch (e: Exception) {
            println("❌ ChatRepo Error creating session: ${e.message}")
            // Au lieu de retourner null et de crash, on relance l'erreur
            // ou on retourne une erreur propre pour que le ViewModel la gère
            throw e
        }
    }

    // Get session details
    override suspend fun getSession(sessionId: String): ChatSession {
        println("ChatRepo fetching session details for sessionId=$sessionId")
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
