package org.julienjnnqin.luxmateapp.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.usecase.LoginUseCase

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun setEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun setPassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login() {
        val currentState = _uiState.value
        if (currentState.email.isEmpty() || currentState.password.isEmpty()) {
            _uiState.value = currentState.copy(error = "Email and password required")
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, error = null)
            val result = loginUseCase(currentState.email, currentState.password)
            result.onSuccess { user ->
                _uiState.value = currentState.copy(isLoading = false, user = user)
            }.onFailure { error ->
                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = error.message ?: "Unknown error"
                )
            }
        }
    }
}

