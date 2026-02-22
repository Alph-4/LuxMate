package org.julienjnnqin.luxmateapp.data.model


import kotlinx.serialization.Serializable
import org.julienjnnqin.luxmateapp.presentation.screen.chat.ChatMessage
import kotlin.time.Clock

@Serializable
data class ChatSession(
    val id: String,
    val personaId: String,
    val personaName: String,
    val personaTheme: String,
    val messageCount: Int = 0,
    val createdAt: String
)

@Serializable
data class CreateChatSessionRequest(
    val personaId: String
)

// Local storage representation with messages
data class ChatSessionWithMessages(
    val session: ChatSession,
    val messages: List<ChatMessage> = emptyList()
)

data class ChatSessionCache(
    val id: String,
    val personaId: String,
    val personaName: String,
    val personaTheme: String,
    val messageCount: Int = 0,
    val createdAt: String,
    val lastSyncedAt: Long = Clock.System.now().epochSeconds,
    val isActive: Boolean = true
)