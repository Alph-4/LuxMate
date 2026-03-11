package org.julienjnnqin.luxmateapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.repository.SettingsRepository
import org.julienjnnqin.luxmateapp.domain.usecase.CheckOnboardingCompletedUseCase
import org.julienjnnqin.luxmateapp.presentation.navigation.Screen

/**
 * État de l'application
 * Détermine la route de démarrage basée sur l'état d'authentification et d'onboarding
 * Compatible avec Jetpack Navigation Compose
 */
data class AppState(
    val isLoading: Boolean = true,
    val startDestination: Screen = Screen.Onboarding,
    val currentLanguage: String = "en"
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
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // On initialise l'état avec les valeurs par défaut
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    init {
        observeAppState()
    }

    private fun observeAppState() {
        viewModelScope.launch {
            // On combine tous les flux importants
            combine(
                settingsRepository.isLoggedIn,
                settingsRepository.currentLanguage,
                // Onboarding n'est souvent pas un Flow, donc on le récupère au besoin
            ) { isLoggedIn, language ->
                val onboardingDone = checkOnboardingCompletedUseCase().getOrDefault(false)

                val destination = when {
                    !onboardingDone -> Screen.Onboarding
                    !isLoggedIn -> Screen.Login
                    else -> Screen.Home
                }

                // On met à jour l'état d'un coup !
                AppState(
                    isLoading = false,
                    startDestination = destination,
                    currentLanguage = language
                )
            }.collect { newState ->
                _appState.value = newState
            }
        }
    }

    // Fonction pour changer la langue depuis l'UI
    fun changeLanguage(langCode: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(langCode)
        }
    }
}