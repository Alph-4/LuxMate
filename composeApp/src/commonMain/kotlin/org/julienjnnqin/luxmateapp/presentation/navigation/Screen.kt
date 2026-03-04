package org.julienjnnqin.luxmateapp.presentation.navigation

var mainRoute = arrayOf(Screen.Home.route, Screen.Personas.route, Screen.SessionsList.route, Screen.Profile.route)

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")

    data object Login : Screen("login")

    data object Home : Screen("home")
    data object Profile : Screen("profile")

    data object Personas : Screen("personas")

    data object PersonaDetail : Screen("personaDetail/{personaId}") {
        const val ARG_PERSONA_ID = "personaId"
        fun createRoute(personaId: String) = "personaDetail/$personaId"
    }

    data object SessionsList : Screen("sessionsList") {
    }

    data object Chat : Screen("chat/{sessionId}") {
        const val ARG_SESSION_ID = "sessionId"
        fun createRoute(sessionId: String) = "chat/$sessionId"
    }
}
