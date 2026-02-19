package org.julienjnnqin.luxmateapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import org.koin.core.context.GlobalContext
import org.julienjnnqin.luxmateapp.core.theme.LuxMateAppTheme
import org.julienjnnqin.luxmateapp.di.initializeKoin
import org.julienjnnqin.luxmateapp.presentation.AppViewModel
import org.julienjnnqin.luxmateapp.presentation.navigation.Screen
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingScreen
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginScreen
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.teachers.TeachersScreen
import org.julienjnnqin.luxmateapp.presentation.screen.teachers.TeachersViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.teacherdetail.TeacherDetailScreen
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileScreen
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileViewModel

@Composable
inline fun <reified T : Any> rememberKoinInstance(): T {
    return remember { GlobalContext.get().get<T>() }
}

@Composable
@Preview
fun App() {
    initializeKoin()

    LuxMateAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val appViewModel: AppViewModel = rememberKoinInstance()
    val appState = appViewModel.appState.collectAsState().value

    when {
        appState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        !appState.isOnboardingCompleted -> {
            val onboardingViewModel: OnboardingViewModel = rememberKoinInstance()
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onComplete = {
                    appViewModel.navigateToLogin()
                }
            )
        }
        appState.currentScreen == Screen.Login -> {
            val loginViewModel: LoginViewModel = rememberKoinInstance()
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    appViewModel.navigateToTeachers()
                }
            )
        }
        appState.currentScreen == Screen.Teachers -> {
            val teachersViewModel: TeachersViewModel = rememberKoinInstance()
            TeachersScreen(
                viewModel = teachersViewModel,
                onTeacherSelected = { teacherId ->
                    appViewModel.navigateToTeacherDetail(teacherId)
                },
                onProfileClick = {
                    appViewModel.navigateToProfile()
                }
            )
        }
        is Screen.TeacherDetail -> {
            val teacherId = (appState.currentScreen as Screen.TeacherDetail).teacherId
            val teachersViewModel: TeachersViewModel = rememberKoinInstance()
            val teacher = teachersViewModel.uiState.collectAsState().value.teachers.find { it.id == teacherId }

            if (teacher != null) {
                TeacherDetailScreen(
                    teacher = teacher,
                    onBackClick = {
                        appViewModel.navigateToTeachers()
                    },
                    onStartConversation = {
                        // TODO: Navigate to chat
                    }
                )
            }
        }
        appState.currentScreen == Screen.Profile -> {
            val profileViewModel: ProfileViewModel = rememberKoinInstance()
            ProfileScreen(
                viewModel = profileViewModel,
                onBackClick = {
                    appViewModel.navigateToTeachers()
                }
            )
        }
    }
}