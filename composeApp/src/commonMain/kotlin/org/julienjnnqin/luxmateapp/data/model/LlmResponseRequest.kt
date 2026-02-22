package org.julienjnnqin.luxmateapp.data.model


import kotlinx.serialization.Serializable

@Serializable
data class LLMResponse(
    val greeting: String,
    val main_point: String,
    val details: List<String>,
    val analogy: String,
    val next_step: String
)

// Pour affichage structuré dans l'UI
data class StructuredAnswer(
    val greeting: String,
    val mainPoint: String,
    val details: List<String>,
    val analogy: String,
    val nextStep: String
) {
    companion object {
        fun fromLLMResponse(response: LLMResponse): StructuredAnswer {
            return StructuredAnswer(
                greeting = response.greeting,
                mainPoint = response.main_point,
                details = response.details,
                analogy = response.analogy,
                nextStep = response.next_step
            )
        }
    }
}