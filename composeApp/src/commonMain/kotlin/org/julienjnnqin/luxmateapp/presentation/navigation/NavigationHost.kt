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
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingScreen
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileScreen
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.teachers.TeachersScreen
import org.julienjnnqin.luxmateapp.presentation.screen.teachers.TeachersViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.teacherdetail.TeacherDetailScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Navigation Manager - Gère l'état de navigation pour l'app
 * Compatible Kotlin Multiplatform
 */
data class NavigationState(
    val currentScreen: Screen = Screen.Onboarding,
    val navHistory: List<Screen> = emptyList()
)

/**
 * Configuration de la navigation avec un NavigationManager
 * Utilise les sealed classes Serializable pour le typage sûr des routes
 * Compatible avec tous les targets Kotlin Multiplatform
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
                onComplete = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = koinViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Teachers.route)
                }
            )
        }

        composable(Screen.Teachers.route) {
            val viewModel: TeachersViewModel = koinViewModel()
            TeachersScreen(
                viewModel = viewModel,
                onTeacherSelected = { teacherId ->
                    navController.navigate(Screen.TeacherDetail.createRoute(teacherId))
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(
            route = Screen.TeacherDetail.route,
            arguments = listOf(
                navArgument(Screen.TeacherDetail.ARG_TEACHER_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viewModel: TeachersViewModel = koinViewModel()
            val uiState = viewModel.uiState.collectAsState().value
            val teacherId = backStackEntry.arguments
            //val teacher = uiState.teachers.firstOrNull { it.id == teacherId }
            val teacher = uiState.teachers.firstOrNull { true }

            if (teacher == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                TeacherDetailScreen(
                    teacher = teacher,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onStartConversation = {
                        // TODO: Navigate to chat screen with teacherId
                        navController.navigate(Screen.Teachers.route)
                    }
                )
            }
        }

        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = koinViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
