package org.julienjnnqin.luxmateapp.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.data.model.SendMessageRequest
import org.julienjnnqin.luxmateapp.domain.entity.Conversation
import org.julienjnnqin.luxmateapp.domain.entity.Message
import org.julienjnnqin.luxmateapp.domain.usecase.GetMessagesUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.GetSessionUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.GetSessionsUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.SendMessageUseCase
import kotlin.random.Random

// Presentation models used by the composables


class ChatViewModel(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getSessionsUseCase: GetSessionsUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {
    private val _sessionsState = MutableStateFlow<List<Conversation>>(emptyList())
    val sessionsState: StateFlow<List<Conversation>> = _sessionsState.asStateFlow()

    private val _messagesState = MutableStateFlow<List<Message>>(emptyList())
    val messagesState: StateFlow<List<Message>> = _messagesState.asStateFlow()

    // typing / thinking indicator
    private val _isThinking = MutableStateFlow(false)
    val isThinking: StateFlow<Boolean> = _isThinking.asStateFlow()

    // Current session id for open chat
    private val _currentSessionIdState = MutableStateFlow("")
    val currentSessionIdState: StateFlow<String> = _currentSessionIdState.asStateFlow()

    // 2. Garde l'accès direct en String pour la logique interne (facultatif mais pratique)
    val currentSessionId: String get() = _currentSessionIdState.value

    init {
        // Load existing sessions on init
        viewModelScope.launch { loadSessions() }
    }

    private suspend fun loadSessions() {
        try {
            val sessions = getSessionsUseCase()
            val convs = sessions.map { s ->
                Conversation(
                    id = s.id,
                    title = s.personaName,
                    lastMessage = if (s.messageCount > 0) "${s.messageCount} messages" else "New session",
                    lastSeen = s.createdAt,
                    unreadCount = 0
                )
            }
            _sessionsState.value = convs
        } catch (e: Exception) {
            e.printStackTrace()
            // keep empty list on error
        }
    }

    fun openSession(sessionId: String) {
        _currentSessionIdState.value = sessionId
        viewModelScope.launch {
            try {
                val messages = getMessagesUseCase(sessionId)
                _messagesState.value = messages.map { m ->
                    Message(id = m.id, content = m.content, timestamp = m.createdAt, isFromUser = m.role == "user")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createSessionAndOpen(sessionId: String) {
        //  if (_isLoading.value || currentSessionId.isNotBlank()) return

        viewModelScope.launch {
            //_isLoading.value = true
            try {
                println("Creating or open session with personaId: $sessionId")
                val session = getSessionUseCase(sessionId)
                println("Session details fetched: id=${session.id}, personaName=${session.personaName}")
                openSession(session.id)
            } catch (e: Exception) {
                println("Session existante ou erreur: ${e.message}")
            } finally {
                //_isLoading.value = false
            }
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            _isThinking.value = true
            try {
                // append user message locally
                val userMsg = Message(
                    id = "local-${Random.nextLong(Long.MAX_VALUE)}",
                    content = content,
                    timestamp = "now",
                    isFromUser = true,

                    )
                val current = _messagesState.value.toMutableList()
                current.add(userMsg)
                _messagesState.value = current.toList()

                val resp = sendMessageUseCase(currentSessionId, SendMessageRequest(content))
                val updated = _messagesState.value.toMutableList()
                updated.add(resp)
                _messagesState.value = updated.toList()

                // refresh sessions preview
                loadSessions()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isThinking.value = false
            }
        }
    }
}
