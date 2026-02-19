package org.julienjnnqin.luxmateapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object Onboarding : Screen("onboarding")

    @Serializable
    data object Login : Screen("login")

    @Serializable
    data object Teachers : Screen("teachers")

    @Serializable
    data object Profile : Screen("profile")

    @Serializable
    data object TeacherDetail : Screen("teacherDetail/{teacherId}") {
        const val ARG_TEACHER_ID = "teacherId"
        fun createRoute(teacherId: String) = "teacherDetail/$teacherId"
    }
}
