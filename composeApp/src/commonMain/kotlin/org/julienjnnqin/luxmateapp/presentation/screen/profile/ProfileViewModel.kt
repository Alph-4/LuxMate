package org.julienjnnqin.luxmateapp.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory
import org.julienjnnqin.luxmateapp.domain.usecase.GetUserProfileUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.GetChatHistoryUseCase

data class ProfileUiState(
    val user: User? = null,
    val chatHistory: List<ChatHistory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val userResult = getUserProfileUseCase()
            val historyResult = getChatHistoryUseCase()

            userResult.onSuccess { user ->
                historyResult.onSuccess { history ->
                    _uiState.value = _uiState.value.copy(
                        user = user,
                        chatHistory = history,
                        isLoading = false
                    )
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        user = user,
                        isLoading = false,
                        error = error.message
                    )
                }
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun refresh() {
        loadProfileData()
    }
}

