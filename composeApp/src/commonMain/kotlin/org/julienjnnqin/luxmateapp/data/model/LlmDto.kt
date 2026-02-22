package org.julienjnnqin.luxmateapp.data.model


import kotlinx.serialization.Serializable

// Deepseek
@Serializable
data class DeepseekMessage(
    val role: String,
    val content: String
)

@Serializable
data class DeepseekRequest(
    val model: String,
    val messages: List<DeepseekMessage>,
    val temperature: Double = 1.0,
    val max_tokens: Int = 4096
)

@Serializable
data class DeepseekChoice(
    val message: DeepseekMessage,
    val finish_reason: String
)

@Serializable
data class DeepseekUsage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

@Serializable
data class DeepseekResponse(
    val id: String,
    @SerialName("object")
    val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<DeepseekChoice>,
    val usage: DeepseekUsage? = null
)

// Mistral
@Serializable
data class MistralMessage(
    val role: String,
    val content: String
)

@Serializable
data class MistralRequest(
    val model: String,
    val messages: List<MistralMessage>
)

@Serializable
data class MistralChatChoice(
    val message: MistralMessage,
    val finish_reason: String? = null
)

@Serializable
data class MistralResponse(
    val id: String,
    val choices: List<MistralChatChoice>,
    val messages: List<MistralMessage>? = null
)