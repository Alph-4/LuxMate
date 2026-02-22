package org.julienjnnqin.luxmateapp.data.model


import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class Persona(
    val id: String,
    val name: String,
    val theme: String,
    val description: String? = null,
    val systemPrompt: String? = null,
    val llmProvider: String = "deepseek",  // deepseek or mistral
    val createdAt: String
)

@Serializable
data class CreatePersonaRequest(
    val name: String,
    val theme: String,
    val description: String? = null,
    val systemPrompt: String = "You are a helpful AI assistant",
    val llmProvider: String = "deepseek"
)

@Serializable
data class UpdatePersonaRequest(
    val name: String,
    val theme: String,
    val description: String? = null,
    val systemPrompt: String,
    val llmProvider: String
)

// Local storage representation
data class PersonaCache(
    val id: String,
    val name: String,
    val theme: String,
    val description: String? = null,
    val systemPrompt: String? = null,
    val llmProvider: String = "deepseek",
    val createdAt: String,
    val lastSyncedAt: Long = Clock.System.now().epochSeconds
)