package org.julienjnnqin.luxmateapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Onboarding : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Teachers : Screen()

    @Serializable
    data object Profile : Screen()

    @Serializable
    data class TeacherDetail(val teacherId: String) : Screen()
}


