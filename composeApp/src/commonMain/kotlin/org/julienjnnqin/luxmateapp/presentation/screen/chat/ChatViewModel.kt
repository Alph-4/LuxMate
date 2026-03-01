package org.julienjnnqin.luxmateapp.presentation.screen.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.data.model.SendMessageRequest
import org.julienjnnqin.luxmateapp.domain.repository.ChatRepository
import kotlin.random.Random

// Presentation models used by the composables
data class Message(val id: String, val content: String, val timestamp: String, val isFromUser: Boolean)

data class Conversation(
    val id: String,
    val title: String,
    val lastMessage: String,
    val lastSeen: String,
    val unreadCount: Int = 0
)

class ChatViewModel(private val personaId: String, private val chatRepository: ChatRepository) : ViewModel() {
    private val scopeJob = Job()
    private val scope = CoroutineScope(scopeJob + Dispatchers.Default)

    private val _sessionsState = MutableStateFlow<List<Conversation>>(emptyList())
    val sessionsState: StateFlow<List<Conversation>> = _sessionsState.asStateFlow()

    private val _messagesState = MutableStateFlow<List<Message>>(emptyList())
    val messagesState: StateFlow<List<Message>> = _messagesState.asStateFlow()

    // typing / thinking indicator
    private val _isThinking = MutableStateFlow(false)
    val isThinking: StateFlow<Boolean> = _isThinking.asStateFlow()

    // Current session id for open chat
    var currentSessionId: String = ""
        private set

    init {
        // Load existing sessions on init
        scope.launch { loadSessions() }
    }

    private suspend fun loadSessions() {
        try {
            val sessions = chatRepository.getSessions()
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
        currentSessionId = sessionId
        scope.launch {
            try {
                val messages = chatRepository.getMessages(sessionId)
                _messagesState.value = messages.map { m ->
                    Message(id = m.id, content = m.content, timestamp = m.createdAt, isFromUser = m.role == "user")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createSessionAndOpen(personaId: String = this.personaId) {
        scope.launch {
            try {
                val session = chatRepository.createSession(personaId)
                // refresh sessions list
                loadSessions()
                openSession(session.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        if (currentSessionId.isBlank()) {
            // create session first
            createSessionAndOpen()
            // wait shortly for session to be created and opened
            scope.launch {
                // naive wait — in practice you'd chain calls
                kotlinx.coroutines.delay(500)
                sendMessage(content)
            }
            return
        }

        scope.launch {
            _isThinking.value = true
            try {
                // append user message locally
                val userMsg = Message(
                    id = "local-${Random.nextLong(Long.MAX_VALUE)}",
                    content = content,
                    timestamp = "now",
                    isFromUser = true
                )
                val current = _messagesState.value.toMutableList()
                current.add(userMsg)
                _messagesState.value = current.toList()

                val resp = chatRepository.sendMessage(currentSessionId, SendMessageRequest(content))

                val assistantText = resp.message ?: resp.structuredResponse?.mainPoint ?: ""
                if (assistantText.isNotBlank()) {
                    val assistantMsg = Message(
                        id = "srv-${Random.nextLong(Long.MAX_VALUE)}",
                        content = assistantText,
                        timestamp = "now",
                        isFromUser = false
                    )
                    val updated = _messagesState.value.toMutableList()
                    updated.add(assistantMsg)
                    _messagesState.value = updated.toList()
                }

                // refresh sessions preview
                loadSessions()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isThinking.value = false
            }
        }
    }

    fun clear() {
        scopeJob.cancel()
    }
}
