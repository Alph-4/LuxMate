package org.julienjnnqin.luxmateapp.di

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.julienjnnqin.luxmateapp.data.repository.OnboardingRepositoryImpl
import org.julienjnnqin.luxmateapp.data.repository.AuthRepositoryImpl
import org.julienjnnqin.luxmateapp.data.repository.TeacherRepositoryImpl
import org.julienjnnqin.luxmateapp.data.repository.UserRepositoryImpl
import org.julienjnnqin.luxmateapp.domain.repository.OnboardingRepository
import org.julienjnnqin.luxmateapp.domain.repository.AuthRepository
import org.julienjnnqin.luxmateapp.domain.repository.TeacherRepository
import org.julienjnnqin.luxmateapp.domain.repository.UserRepository
import org.julienjnnqin.luxmateapp.domain.usecase.*
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.teachers.TeachersViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileViewModel

val appModule = module {
    // Repositories
    single<OnboardingRepository> { OnboardingRepositoryImpl() }
    single<AuthRepository> { AuthRepositoryImpl() }
    single<TeacherRepository> { TeacherRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }

    // Use Cases
    single { CheckOnboardingCompletedUseCase(get()) }
    single { SetOnboardingCompletedUseCase(get()) }
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { GetCurrentUserUseCase(get()) }
    single { GetAllTeachersUseCase(get()) }
    single { SearchTeachersUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
    single { GetChatHistoryUseCase(get()) }

    // ViewModels
    single { OnboardingViewModel(get()) }
    single { LoginViewModel(get()) }
    single { TeachersViewModel(get(), get()) }
    single { ProfileViewModel(get(), get()) }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}

