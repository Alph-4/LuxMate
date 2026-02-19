package org.julienjnnqin.luxmateapp.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.entity.Teacher
import org.julienjnnqin.luxmateapp.domain.usecase.GetTeacherByIdUseCase

data class ChatUiState(
    val teacher: Teacher? = null,
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

class ChatViewModel(
    private val teacherId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadTeacher()
    }

    private fun loadTeacher() {
        viewModelScope.launch {
            // Mock teacher - à remplacer par usecase
            _uiState.value = _uiState.value.copy(
                teacher = null, // Load from usecase
                isLoading = false,
                messages = mockMessages()
            )
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
                content = "Bien sûr! Une dérivée mesure le taux de changement d'une fonction.",
                isFromUser = false,
                timestamp = "10:33"
            )
        )
    }

    fun sendMessage(content: String) {
        val newMessage = ChatMessage(
            id = System.currentTimeMillis().toString(),
            content = content,
            isFromUser = true,
            timestamp = "now"
        )
        val currentMessages = _uiState.value.messages.toMutableList()
        currentMessages.add(newMessage)
        _uiState.value = _uiState.value.copy(messages = currentMessages)
    }
}

