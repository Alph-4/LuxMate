package org.julienjnnqin.luxmateapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.julienjnnqin.luxmateapp.core.theme.LuxMateAppTheme
import org.julienjnnqin.luxmateapp.presentation.AppViewModel
import org.julienjnnqin.luxmateapp.presentation.components.BottomNavigationBar
import org.julienjnnqin.luxmateapp.presentation.navigation.NavigationHost
import org.julienjnnqin.luxmateapp.presentation.navigation.Screen
import org.julienjnnqin.luxmateapp.presentation.navigation.isMainRoute
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    LuxMateAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    val appViewModel: AppViewModel = koinViewModel()
    val appState by appViewModel.appState.collectAsState()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        println("Current route: $currentRoute")
    }

    if (appState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            bottomBar = {
                // ✅ Vérifie si on est sur une route principale
                if (currentRoute.isMainRoute()) {
                    BottomNavigationBar(
                        selectedIndex = when {
                            currentRoute?.contains("Home") == true -> 0
                            currentRoute?.contains("Personas") == true -> 1
                            currentRoute?.contains("SessionsList") == true -> 2
                            currentRoute?.contains("Profile") == true -> 3
                            else -> 0
                        },
                        onItemSelected = { index ->
                            when (index) {
                                0 -> navController.navigate(Screen.Home) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                1 -> navController.navigate(Screen.Personas) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                2 -> navController.navigate(Screen.SessionsList) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                3 -> navController.navigate(Screen.Profile) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            NavigationHost(
                navController = navController,
                startDestination = appState.startDestination,
                modifier = Modifier
            )
        }
    }
}