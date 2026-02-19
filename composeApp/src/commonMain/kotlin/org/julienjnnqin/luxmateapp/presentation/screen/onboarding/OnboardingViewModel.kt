package org.julienjnnqin.luxmateapp.presentation.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.usecase.SetOnboardingCompletedUseCase

data class OnboardingUiState(
    val currentPage: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class OnboardingViewModel(
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun nextPage() {
        val currentState = _uiState.value
        if (currentState.currentPage < 2) {
            _uiState.value = currentState.copy(currentPage = currentState.currentPage + 1)
        }
    }

    fun previousPage() {
        val currentState = _uiState.value
        if (currentState.currentPage > 0) {
            _uiState.value = currentState.copy(currentPage = currentState.currentPage - 1)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = setOnboardingCompletedUseCase()
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun skipOnboarding() {
        completeOnboarding()
    }
}

