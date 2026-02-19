package org.julienjnnqin.luxmateapp.domain.usecase

import org.julienjnnqin.luxmateapp.domain.entity.OnboardingState
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.entity.Teacher
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory
import org.julienjnnqin.luxmateapp.domain.repository.OnboardingRepository
import org.julienjnnqin.luxmateapp.domain.repository.AuthRepository
import org.julienjnnqin.luxmateapp.domain.repository.TeacherRepository
import org.julienjnnqin.luxmateapp.domain.repository.UserRepository

class CheckOnboardingCompletedUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): Result<Boolean> = runCatching {
        onboardingRepository.getOnboardingState().isCompleted
    }
}

class SetOnboardingCompletedUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return onboardingRepository.setOnboardingCompleted()
    }
}

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }
}

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User?> {
        return authRepository.getCurrentUser()
    }
}

class GetAllTeachersUseCase(
    private val teacherRepository: TeacherRepository
) {
    suspend operator fun invoke(): Result<List<Teacher>> {
        return teacherRepository.getAllTeachers()
    }
}

class SearchTeachersUseCase(
    private val teacherRepository: TeacherRepository
) {
    suspend operator fun invoke(query: String): Result<List<Teacher>> {
        return teacherRepository.searchTeachers(query)
    }
}

class GetUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        return userRepository.getUserProfile()
    }
}

class GetChatHistoryUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<ChatHistory>> {
        return userRepository.getChatHistory()
    }
}

