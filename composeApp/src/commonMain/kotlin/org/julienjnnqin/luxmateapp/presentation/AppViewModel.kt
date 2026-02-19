package org.julienjnnqin.luxmateapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.usecase.CheckOnboardingCompletedUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.GetCurrentUserUseCase
import org.julienjnnqin.luxmateapp.presentation.navigation.Screen

data class AppState(
    val isOnboardingCompleted: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = true,
    val currentScreen: Screen = Screen.Onboarding
)

class AppViewModel(
    private val checkOnboardingCompletedUseCase: CheckOnboardingCompletedUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    init {
        viewModelScope.launch {
            checkOnboardingStatus()
        }
    }

    private suspend fun checkOnboardingStatus() {
        val result = checkOnboardingCompletedUseCase()
        result.onSuccess { isCompleted ->
            if (isCompleted) {
                checkAuthStatus()
            } else {
                _appState.value = _appState.value.copy(
                    isOnboardingCompleted = false,
                    isLoading = false,
                    currentScreen = Screen.Onboarding
                )
            }
        }.onFailure {
            _appState.value = _appState.value.copy(isLoading = false)
        }
    }

    private suspend fun checkAuthStatus() {
        val result = getCurrentUserUseCase()
        result.onSuccess { user ->
            if (user != null) {
                _appState.value = _appState.value.copy(
                    isOnboardingCompleted = true,
                    isAuthenticated = true,
                    isLoading = false,
                    currentScreen = Screen.Teachers
                )
            } else {
                _appState.value = _appState.value.copy(
                    isOnboardingCompleted = true,
                    isAuthenticated = false,
                    isLoading = false,
                    currentScreen = Screen.Login
                )
            }
        }.onFailure {
            _appState.value = _appState.value.copy(
                isOnboardingCompleted = true,
                isAuthenticated = false,
                isLoading = false,
                currentScreen = Screen.Login
            )
        }
    }

    fun navigateToTeachers() {
        _appState.value = _appState.value.copy(
            isAuthenticated = true,
            currentScreen = Screen.Teachers
        )
    }

    fun navigateToLogin() {
        _appState.value = _appState.value.copy(
            currentScreen = Screen.Login
        )
    }

    fun navigateToProfile() {
        _appState.value = _appState.value.copy(
            currentScreen = Screen.Profile
        )
    }

    fun navigateToOnboarding() {
        _appState.value = _appState.value.copy(
            currentScreen = Screen.Onboarding
        )
    }

    fun navigateToTeacherDetail(teacherId: String) {
        _appState.value = _appState.value.copy(
            currentScreen = Screen.TeacherDetail(teacherId)
        )
    }
}


