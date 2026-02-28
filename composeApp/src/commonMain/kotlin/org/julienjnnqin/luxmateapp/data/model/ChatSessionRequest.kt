package org.julienjnnqin.luxmateapp.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.julienjnnqin.luxmateapp.presentation.screen.chat.ChatMessage
import kotlin.time.Clock

@Serializable
data class ChatSession(
    val id: String,
    @SerialName("persona_id")
    val personaId: String,
    @SerialName("persona_name")
    val personaName: String,
    @SerialName("persona_theme")
    val personaTheme: String,
    @SerialName("message_count")
    val messageCount: Int = 0,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class CreateChatSessionRequest(
    @SerialName("persona_id")
    val personaId: String
)

// Local storage representation with messages
data class ChatSessionWithMessages(
    val session: ChatSession,
    val messages: List<ChatMessage> = emptyList()
)
