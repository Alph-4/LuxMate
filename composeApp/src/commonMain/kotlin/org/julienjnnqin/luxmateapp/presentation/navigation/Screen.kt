package org.julienjnnqin.luxmateapp.presentation.navigation

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

// Routes principales pour la bottom navigation
val mainRoutes: List<KClass<out Screen>> = listOf(
    Screen.Home::class,
    Screen.Personas::class,
    Screen.SessionsList::class,
    Screen.Profile::class
)

@Serializable
sealed interface Screen {
    @Serializable
    data object Onboarding : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Personas : Screen

    @Serializable
    data class PersonaDetail(val personaId: String) : Screen

    @Serializable
    data object SessionsList : Screen

    @Serializable
    data class Chat(val sessionId: String) : Screen

    @Serializable
    data object Profile : Screen
}

// Extension pour vérifier si une route est dans mainRoutes
fun String?.isMainRoute(): Boolean {
    if (this == null) return false
    return this.contains("Home") ||
            this.contains("Personas") ||
            this.contains("SessionsList") ||
            this.contains("Profile")
}