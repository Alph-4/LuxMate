package org.julienjnnqin.luxmateapp.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.data.model.SendMessageRequest
import org.julienjnnqin.luxmateapp.domain.entity.Persona
import org.julienjnnqin.luxmateapp.domain.repository.ChatRepository

data class ChatUiState(
    val persona: Persona? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val messages: List<ChatMessage> = emptyList()
)

data class ChatMessage(
        val id: String,
        val content: String,
        val isFromUser: Boolean,
        val timestamp: String
)

class ChatViewModel(private val teacherId: String, private val chatRepository: ChatRepository) :
        ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadTeacher()
    }

    private fun loadTeacher() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Create or get a session for this persona/teacher
                val session = chatRepository.createSession(teacherId)
                val messages = chatRepository.getMessages(session.id)
                val uiMessages =
                        messages.map { m ->
                            ChatMessage(
                                    id = m.id,
                                    content = m.content,
                                    isFromUser = m.role == "user",
                                    timestamp = m.createdAt
                            )
                        }
                _uiState.value =
                        _uiState.value.copy(
                                persona = null,
                                isLoading = false,
                                messages = uiMessages
                        )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun mockMessages(): List<ChatMessage> {
        return listOf(
                ChatMessage(
                        id = "1",
                        content = "Bonjour! Je suis Pierre, votre professeur de mathématiques.",
                        isFromUser = false,
                        timestamp = "10:30"
                ),
                ChatMessage(
                        id = "2",
                        content = "Comment puis-je vous aider aujourd'hui?",
                        isFromUser = false,
                        timestamp = "10:31"
                ),
                ChatMessage(
                        id = "3",
                        content = "J'aimerais comprendre les dérivées",
                        isFromUser = true,
                        timestamp = "10:32"
                ),
                ChatMessage(
                        id = "4",
                        content =
                                "Bien sûr! Une dérivée mesure le taux de changement d'une fonction.",
                        isFromUser = false,
                        timestamp = "10:33"
                )
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun sendMessage(content: String) {
        viewModelScope.launch {
            try {
                // Ensure session exists by creating with personaId
                val session = chatRepository.createSession(teacherId)
                val response = chatRepository.sendMessage(session.id, SendMessageRequest(content))
                val assistantText =
                        response.message ?: response.structuredResponse?.toString() ?: ""
                val currentMessages = _uiState.value.messages.toMutableList()
                // Add user message locally
                currentMessages.add(
                        ChatMessage(
                                id = Uuid.generateV4().toString(),
                                content = content,
                                isFromUser = true,
                                timestamp = "now"
                        )
                )
                // Add assistant reply
                currentMessages.add(
                        ChatMessage(
                                id = Uuid.generateV4().toString(),
                                content = assistantText,
                                isFromUser = false,
                                timestamp = "now"
                        )
                )
                _uiState.value = _uiState.value.copy(messages = currentMessages)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
