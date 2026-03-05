package org.julienjnnqin.luxmateapp.domain.usecase

import org.julienjnnqin.luxmateapp.data.model.SendMessageRequest
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory
import org.julienjnnqin.luxmateapp.domain.entity.Persona
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.repository.*

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

class LoginWithGoogleUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<User> {
        return repository.googleSignIn()
    }
}

class IsUserLoggedInUseCase(private val tokenStore: SettingsRepository) {
    suspend operator fun invoke(): Boolean {
        val token = tokenStore.getAccessToken()
        return !token.isNullOrBlank()
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
    suspend operator fun invoke(): Result<List<Persona>> {
        return teacherRepository.getAllTeachers()
    }
}

class SearchTeachersUseCase(
    private val teacherRepository: TeacherRepository
) {
    suspend operator fun invoke(query: String): Result<List<Persona>> {
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

class GetMessagesUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(sessionId: String) = repository.getMessages(sessionId)
}

class GetSessionsUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke() = repository.getSessions()
}

class GetSessionUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(sessionId: String) = repository.getSession(sessionId)
}

class SendMessageUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(sessionId: String, request: SendMessageRequest) =
        repository.sendMessage(sessionId, request)
}

