package org.julienjnnqin.luxmateapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginScreen
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.chat.ChatScreen
import org.julienjnnqin.luxmateapp.presentation.screen.chat.ChatViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.home.HomeScreen
import org.julienjnnqin.luxmateapp.presentation.screen.home.HomeViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingScreen
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.personas.PersonaDetailScreen
import org.julienjnnqin.luxmateapp.presentation.screen.personas.PersonasScreen
import org.julienjnnqin.luxmateapp.presentation.screen.personas.PersonasViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileScreen
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


/**
 * Configuration de la navigation avec un NavigationManager Utilise les sealed classes Serializable
 * pour le typage sûr des routes Compatible avec tous les targets Kotlin Multiplatform
 */
@Composable
fun NavigationHost(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(Screen.Onboarding.route) {
            val viewModel: OnboardingViewModel = koinViewModel()
            OnboardingScreen(
                viewModel = viewModel,
                onComplete = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = koinViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { navController.navigate(Screen.Home.route) }
            )
        }

        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = koinViewModel()
            HomeScreen(
                "John  Doe",
                viewModel = viewModel
            )
        }

        composable(Screen.Personas.route) {
            val viewModel: PersonasViewModel = koinViewModel()
            PersonasScreen(
                viewModel = viewModel,
                onPersonaSelected = { personaId ->
                    navController.navigate(Screen.PersonaDetail.createRoute(personaId))
                }
            )
        }

        composable(
            route = Screen.PersonaDetail.route,
            arguments =
                listOf(
                    navArgument(Screen.PersonaDetail.ARG_PERSONA_ID) {
                        type = NavType.StringType
                    }
                )
        ) { backStackEntry ->
            val personaId =
                backStackEntry.arguments?.getString(Screen.PersonaDetail.ARG_PERSONA_ID) ?: ""
            val viewModel: PersonasViewModel = koinViewModel()
            val uiState = viewModel.uiState.collectAsState().value
            val persona = uiState.personas.firstOrNull { it.id == personaId }
            if (persona == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                PersonaDetailScreen(
                    persona = persona,
                    onBackClick = { navController.popBackStack() },
                    onStartConversation = { pId ->
                        navController.navigate(Screen.Chat.createRoute(pId))
                    }
                )
            }
        }

        composable(
            route = Screen.Chat.route,
            arguments =
                listOf(
                    navArgument(Screen.Chat.ARG_PERSONA_ID) {
                        type = NavType.StringType
                    }
                )
        ) { backStackEntry ->
            val personaId = backStackEntry.arguments?.getString(Screen.Chat.ARG_PERSONA_ID) ?: ""
            // Explicitly pass <ChatViewModel> here:
            val viewModel = koinViewModel<ChatViewModel> { parametersOf(personaId) }
            ChatScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = koinViewModel()
            ProfileScreen(viewModel = viewModel, onBackClick = { navController.popBackStack() })
        }
    }
}
