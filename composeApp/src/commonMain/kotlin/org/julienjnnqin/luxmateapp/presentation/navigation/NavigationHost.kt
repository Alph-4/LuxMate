package org.julienjnnqin.luxmateapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginScreen
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.chat.ChatDetailScreen
import org.julienjnnqin.luxmateapp.presentation.screen.chat.ChatViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.chat.SessionsListScreen
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

@Composable
fun NavigationHost(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Screen.Onboarding> {
            val viewModel: OnboardingViewModel = koinViewModel()
            OnboardingScreen(
                viewModel = viewModel,
                onComplete = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Onboarding) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Login> {
            val viewModel: LoginViewModel = koinViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Home> {
            val viewModel: HomeViewModel = koinViewModel()
            HomeScreen(
                viewModel = viewModel,
                recentMsgSeeAllClick = {
                    navController.navigate(Screen.Personas)
                }
            )
        }

        composable<Screen.Personas> {
            val viewModel: PersonasViewModel = koinViewModel()
            PersonasScreen(
                viewModel = viewModel,
                onPersonaSelected = { personaId ->
                    // ✅ Navigation typée - pas de String!
                    navController.navigate(Screen.PersonaDetail(personaId))
                },
                startChatWithPersona = { personaId ->
                    println("Quick start chat with selected personaId: $personaId")
                    navController.navigate(Screen.Chat(personaId))
                }
            )
        }

        composable<Screen.PersonaDetail> { backStackEntry ->
            // ✅ Récupération typée des arguments
            val args = backStackEntry.toRoute<Screen.PersonaDetail>()
            val personaId = args.personaId

            val viewModel: PersonasViewModel = koinViewModel()
            val uiState = viewModel.uiState.collectAsState().value
            val persona = uiState.personas.firstOrNull { it.id == personaId }

            if (persona == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                PersonaDetailScreen(
                    persona = persona,
                    onBackClick = { navController.popBackStack() },
                    onStartConversation = { pId ->
                        println("Start chat with selected personaId: $pId")
                        navController.navigate(Screen.Chat(pId))
                    }
                )
            }
        }

        composable<Screen.SessionsList> {
            val viewModel: ChatViewModel = koinViewModel()
            SessionsListScreen(
                viewModel = viewModel,
                onOpenConversation = { sessionId ->
                    navController.navigate(Screen.Chat(sessionId))
                }
            )
        }

        composable<Screen.Chat> { backStackEntry ->
            // ✅ Récupération typée des arguments
            val args = backStackEntry.toRoute<Screen.Chat>()
            val sessionId = args.sessionId

            val viewModel: ChatViewModel = koinViewModel()
            ChatDetailScreen(
                viewModel = viewModel,
                sessionId = sessionId,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Screen.Profile> {
            val viewModel: ProfileViewModel = koinViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onOpenConversation = { sessionId ->
                    navController.navigate(Screen.Chat(sessionId))
                },
                onLogout = {
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}