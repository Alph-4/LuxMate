package org.julienjnnqin.luxmateapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.julienjnnqin.luxmateapp.core.theme.LuxMateAppTheme
import org.julienjnnqin.luxmateapp.di.initializeKoin
import org.julienjnnqin.luxmateapp.presentation.AppViewModel
import org.julienjnnqin.luxmateapp.presentation.navigation.NavigationHost
import androidx.navigation.compose.rememberNavController
import org.julienjnnqin.luxmateapp.presentation.components.BottomNavigationBar
import org.julienjnnqin.luxmateapp.presentation.navigation.Screen
import org.julienjnnqin.luxmateapp.presentation.navigation.mainRoute

/**
 * Point d'entrée de l'application
 * Initialise Koin et affiche le thème
 * Gère l'état de chargement initial et la navigation
 */
@Composable
@Preview
fun App() {
    initializeKoin()

    LuxMateAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppContent()
        }
    }
}

/**
 * Contenu principal de l'application
 * Affiche le spinner de chargement ou la navigation
 * Détermine la destination initiale basée sur l'état de l'app
 */
@Composable
fun AppContent() {
    // ViewModel de l'app pour déterminer la destination initiale
    val appViewModel: AppViewModel = koinViewModel()
    val appState = appViewModel.appState.collectAsState().value
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(navController) {
        println("Current route: ${navController.currentDestination?.route}")
    }

    if (appState.isLoading) {
        // Affiche un spinner pendant le chargement de l'état initial
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Lance la navigation avec la destination initiale déterminée

        Scaffold (
            bottomBar = {
                if(currentRoute in mainRoute) {
                    BottomNavigationBar(
                        selectedIndex = when (currentRoute) {
                            Screen.Teachers.route -> 0
                            Screen.Profile.route -> 1
                            else -> 0
                        },
                        onItemSelected = {
                            when (it) {
                                0 -> navController.navigate(Screen.Teachers.route)
                                1 -> navController.navigate(Screen.Profile.route)
                                2 -> navController.navigate(Screen.Profile.route)
                            }
                        },
                    )}
            }
        ) {

            NavigationHost(
                navController = navController,
                startDestination = appState.startDestination
            )

        }
    }
}