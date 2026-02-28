package org.julienjnnqin.luxmateapp.presentation.navigation

import kotlinx.serialization.Serializable

var mainRoute = arrayOf(Screen.Personas.route, Screen.Profile.route, Screen.Profile.route)

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object Onboarding : Screen("onboarding")

    @Serializable
    data object Login : Screen("login")

    data object Home : Screen("home")
    data object Profile : Screen("profile")

    @Serializable
    data object Personas : Screen("personas")

    @Serializable
    data object PersonaDetail : Screen("personaDetail/{personaId}") {
        const val ARG_PERSONA_ID = "personaId"
        fun createRoute(personaId: String) = "personaDetail/$personaId"
    }

    @Serializable
    data object Chat : Screen("chat/{personaId}") {
        const val ARG_PERSONA_ID = "personaId"
        fun createRoute(personaId: String) = "chat/$personaId"
    }
}
