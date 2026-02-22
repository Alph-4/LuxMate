package org.julienjnnqin.luxmateapp.data.model


import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String,
    val role: String,  // "user" or "assistant"
    val content: String,
    val structuredResponse: LLMResponse? = null,
    val createdAt: String
)

@Serializable
data class SendMessageRequest(
    val content: String
)

@Serializable
data class SendMessageResponse(
    val message: String? = null,
    val structuredResponse: LLMResponse? = null,
    val role: String = "assistant"
)

// Local storage representation
data class ChatMessageCache(
    val id: String,
    val sessionId: String,
    val role: String,
    val content: String,
    val structuredResponse: LLMResponse? = null,
    val createdAt: String,
    val isSynced: Boolean = true
)