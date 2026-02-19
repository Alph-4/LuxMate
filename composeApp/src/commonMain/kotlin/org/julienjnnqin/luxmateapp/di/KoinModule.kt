package org.julienjnnqin.luxmateapp.di

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.compose.viewmodel.dsl.viewModel
import org.julienjnnqin.luxmateapp.data.repository.OnboardingRepositoryImpl
import org.julienjnnqin.luxmateapp.data.repository.AuthRepositoryImpl
import org.julienjnnqin.luxmateapp.data.repository.TeacherRepositoryImpl
import org.julienjnnqin.luxmateapp.data.repository.UserRepositoryImpl
import org.julienjnnqin.luxmateapp.domain.repository.OnboardingRepository
import org.julienjnnqin.luxmateapp.domain.repository.AuthRepository
import org.julienjnnqin.luxmateapp.domain.repository.TeacherRepository
import org.julienjnnqin.luxmateapp.domain.repository.UserRepository
import org.julienjnnqin.luxmateapp.domain.usecase.*
import org.julienjnnqin.luxmateapp.presentation.AppViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.teachers.TeachersViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileViewModel

/**
 * Module Koin pour l'injection de dépendances
 * Suit les meilleures pratiques Android:
 * - Repositories et Use Cases: single (instance unique partagée)
 * - ViewModels: viewModel() (instance unique par écran via Jetpack ViewModel)
 */
val appModule = module {
    // ===== REPOSITORIES =====
    // Single pour les repositories : une seule instance partagée
    single<OnboardingRepository> { OnboardingRepositoryImpl() }
    single<AuthRepository> { AuthRepositoryImpl() }
    single<TeacherRepository> { TeacherRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }

    // ===== USE CASES =====
    // Single pour les use cases : injection déclarative
    single { CheckOnboardingCompletedUseCase(get()) }
    single { SetOnboardingCompletedUseCase(get()) }
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { GetCurrentUserUseCase(get()) }
    single { GetAllTeachersUseCase(get()) }
    single { SearchTeachersUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
    single { GetChatHistoryUseCase(get()) }

    // ===== VIEW MODELS =====
    // viewModel() pour les ViewModels : gestion du cycle de vie par Compose
    // Une instance par écran, détruite quand on quitte l'écran

    viewModel { AppViewModel(get()) }

    viewModel { OnboardingViewModel(get()) }

    viewModel { LoginViewModel(get()) }

    viewModel { TeachersViewModel(get(), get()) }

    viewModel { ProfileViewModel(get(), get()) }
}

/**
 * Initialise Koin avec le module de l'application
 * À appeler une seule fois au démarrage de l'app
 */
fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}

