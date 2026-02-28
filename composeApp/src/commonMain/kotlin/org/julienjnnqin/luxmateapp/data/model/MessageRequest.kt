package org.julienjnnqin.luxmateapp.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String,
    val role: String,  // "user" or "assistant"
    val content: String,
    @SerialName("structured_response")
    val structuredResponse: LLMResponse? = null,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class SendMessageRequest(
    val content: String
)

@Serializable
data class SendMessageResponse(
    val message: String? = null,
    @SerialName("structured_response")
    val structuredResponse: LLMResponse? = null,
    val role: String = "assistant"
)

