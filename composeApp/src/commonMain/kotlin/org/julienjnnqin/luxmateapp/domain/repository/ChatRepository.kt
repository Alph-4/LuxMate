package org.julienjnnqin.luxmateapp.domain.repository

import org.julienjnnqin.luxmateapp.data.model.ChatMessage
import org.julienjnnqin.luxmateapp.data.model.ChatSession
import org.julienjnnqin.luxmateapp.data.model.SendMessageRequest
import org.julienjnnqin.luxmateapp.data.model.SendMessageResponse

interface ChatRepository {
    suspend fun getSessions(): List<ChatSession>
    suspend fun createSession(personaId: String): ChatSession
    suspend fun getSession(sessionId: String): ChatSession
    suspend fun getMessages(sessionId: String): List<ChatMessage>
    suspend fun sendMessage(sessionId: String, request: SendMessageRequest): SendMessageResponse
}
