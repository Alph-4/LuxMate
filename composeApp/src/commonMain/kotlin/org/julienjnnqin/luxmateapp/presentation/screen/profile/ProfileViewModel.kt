package org.julienjnnqin.luxmateapp.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.usecase.GetChatHistoryUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.GetUserProfileUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.LogoutUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.SetUserLanguageUseCase

data class ProfileUiState(
    val user: User? = null,
    val chatHistory: List<ChatHistory> = emptyList(),
    val isLoading: Boolean = false,
    val isLoggingOut: Boolean = false,
    val loggedOut: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val setUserLanguageUseCase: SetUserLanguageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun setLanguage(language: String) {
        viewModelScope.launch {
            setUserLanguageUseCase(language)
        }
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

    fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoggingOut = true,
                loggedOut = false,
                error = null
            )
            logoutUseCase()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoggingOut = false,
                        loggedOut = true,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoggingOut = false,
                        loggedOut = false,
                        error = error.message
                    )
                }
        }
    }

    /**
     * Call this from the UI after handling the logout navigation event to
     * reset the one-shot `loggedOut` flag and avoid re-triggering navigation
     * on recomposition or state restoration.
     */
    fun onLogoutHandled() {
        _uiState.value = _uiState.value.copy(loggedOut = false)
    }
}

