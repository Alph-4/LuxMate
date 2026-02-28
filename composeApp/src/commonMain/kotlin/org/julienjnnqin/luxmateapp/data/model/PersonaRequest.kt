package org.julienjnnqin.luxmateapp.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class Persona(
    val id: String,
    val name: String,
    val theme: String,
    val description: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("system_prompt")
    val systemPrompt: String? = null,
    @SerialName("llm_provider")
    val llmProvider: String = "deepseek",  // deepseek or mistral
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class CreatePersonaRequest(
    val name: String,
    val theme: String,
    val description: String? = null,
    @SerialName("system_prompt")
    val systemPrompt: String = "You are a helpful AI assistant",
    @SerialName("llm_provider")
    val llmProvider: String = "deepseek"
)

@Serializable
data class UpdatePersonaRequest(
    val name: String,
    val theme: String,
    val description: String? = null,
    @SerialName("system_prompt")
    val systemPrompt: String,
    @SerialName("llm_provider")
    val llmProvider: String
)