package org.julienjnnqin.luxmateapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.usecase.CheckOnboardingCompletedUseCase
import org.julienjnnqin.luxmateapp.presentation.navigation.Screen

/**
 * État de l'application
 * Détermine la route de démarrage basée sur l'état d'authentification et d'onboarding
 * Compatible avec Jetpack Navigation Compose
 */
data class AppState(
    val isLoading: Boolean = true,
    val startDestination: Screen = Screen.Onboarding
)

/**
 * ViewModel de l'application
 * Gère l'état global de navigation et la logique de démarrage
 * Détermine la route initiale en fonction du statut d'onboarding et d'authentification
 *
 * Bonnes pratiques:
 * - Utilise viewModelScope pour les tâches asynchrones
 * - Expose uniquement des StateFlow (immutable de l'extérieur)
 * - Ne gère pas la navigation directement (c'est le rôle de NavController)
 * - Détermine juste la destination de démarrage
 */
class AppViewModel(
    private val checkOnboardingCompletedUseCase: CheckOnboardingCompletedUseCase
) : ViewModel() {

    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    init {
        loadInitialDestination()
    }

    /**
     * Détermine la destination initiale basée sur le statut d'onboarding
     */
    private fun loadInitialDestination() {
        viewModelScope.launch {
            try {
                val result = checkOnboardingCompletedUseCase()
                val startDestination = result.fold(
                    onSuccess = { isCompleted ->
                        if (isCompleted) Screen.Login else Screen.Onboarding
                    },
                    onFailure = { Screen.Onboarding }
                )

                _appState.value = AppState(
                    isLoading = false,
                    startDestination = startDestination
                )
            } catch (_: Exception) {
                // En cas d'erreur, afficher l'onboarding par défaut
                _appState.value = AppState(
                    isLoading = false,
                    startDestination = Screen.Onboarding
                )
            }
        }
    }
}


