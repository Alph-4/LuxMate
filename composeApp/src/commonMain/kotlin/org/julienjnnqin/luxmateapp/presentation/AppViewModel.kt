package org.julienjnnqin.luxmateapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.repository.SettingsRepository
import org.julienjnnqin.luxmateapp.domain.usecase.CheckOnboardingCompletedUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.IsUserLoggedInUseCase
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
    private val checkOnboardingCompletedUseCase: CheckOnboardingCompletedUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    init {
        viewModelScope.launch {
            // On combine l'état d'onboarding et le flux de connexion
            settingsRepository.isLoggedIn.collect { loggedIn ->
                val onboardingDone = checkOnboardingCompletedUseCase().getOrDefault(false)

                val destination = when {
                    !onboardingDone -> Screen.Onboarding
                    !loggedIn -> Screen.Login
                    else -> Screen.Home
                }

                _appState.value = AppState(isLoading = false, startDestination = destination)
            }
        }
    }

    /**
     * Détermine la destination initiale basée sur le statut d'onboarding
     */
    private fun loadInitialDestination() {
        viewModelScope.launch {
            try {

                // 1. On vérifie l'onboarding
                val onboardingDone = checkOnboardingCompletedUseCase().getOrDefault(false)

                // 2. On vérifie si un token existe
                val isLoggedIn = isUserLoggedInUseCase()

                // 3. On calcule la destination
                val destination = when {
                    !onboardingDone -> Screen.Onboarding
                    !isLoggedIn -> Screen.Login
                    else -> Screen.Home // L'auto-login se produit ici
                }
                _appState.value = AppState(
                    isLoading = false,
                    startDestination = destination
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


