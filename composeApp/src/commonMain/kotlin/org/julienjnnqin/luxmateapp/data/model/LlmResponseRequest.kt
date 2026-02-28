package org.julienjnnqin.luxmateapp.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LLMResponse(
    val greeting: String,
    @SerialName("main_point")
    val mainPoint: String,
    val details: List<String>,
    val analogy: String,
    @SerialName("next_step")
    val nextStep: String
)

// Pour affichage structuré dans l'UI
data class StructuredAnswer(
    val greeting: String,
    @SerialName("main_point")
    val mainPoint: String,
    val details: List<String>,
    val analogy: String,
    @SerialName("next_step")
    val nextStep: String
) {
    companion object {
        fun fromLLMResponse(response: LLMResponse): StructuredAnswer {
            return StructuredAnswer(
                greeting = response.greeting,
                mainPoint = response.mainPoint,
                details = response.details,
                analogy = response.analogy,
                nextStep = response.nextStep
            )
        }
    }
}