package org.julienjnnqin.luxmateapp.presentation.navigation

import kotlinx.serialization.Serializable

var mainRoute = arrayOf(Screen.Teachers.route, Screen.Profile.route, Screen.Profile.route)

@Serializable
sealed class Screen(val route: String, val index: Int? = null) {
    @Serializable data object Onboarding : Screen("onboarding", index = 0)

    @Serializable data object Login : Screen("login")

    @Serializable data object Teachers : Screen("teachers")

    @Serializable data object Profile : Screen("profile")

    @Serializable
    data object TeacherDetail : Screen("teacherDetail/{teacherId}") {
        const val ARG_TEACHER_ID = "teacherId"
        fun createRoute(teacherId: String) = "teacherDetail/$teacherId"
    }

    @Serializable data object Personas : Screen("personas")

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
