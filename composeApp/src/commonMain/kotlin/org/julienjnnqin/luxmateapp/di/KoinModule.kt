package org.julienjnnqin.luxmateapp.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.json
import org.julienjnnqin.luxmateapp.data.auth.InMemoryTokenStore
import org.julienjnnqin.luxmateapp.data.auth.TokenStore
import org.julienjnnqin.luxmateapp.data.config.JsonConfig
import org.julienjnnqin.luxmateapp.data.remote.KtorbackendApi
import org.julienjnnqin.luxmateapp.data.remote.BackendApi
import org.julienjnnqin.luxmateapp.data.repository.AuthRepositoryImpl
import org.julienjnnqin.luxmateapp.data.repository.OnboardingRepositoryImpl
import org.julienjnnqin.luxmateapp.data.repository.TeacherRepositoryImpl
import org.julienjnnqin.luxmateapp.data.repository.UserRepositoryImpl
import org.julienjnnqin.luxmateapp.domain.repository.AuthRepository
import org.julienjnnqin.luxmateapp.domain.repository.OnboardingRepository
import org.julienjnnqin.luxmateapp.domain.repository.TeacherRepository
import org.julienjnnqin.luxmateapp.domain.repository.UserRepository
import org.julienjnnqin.luxmateapp.domain.usecase.*
import org.julienjnnqin.luxmateapp.presentation.AppViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.chat.ChatViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.personas.PersonasViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Module Koin pour l'injection de dépendances Suit les meilleures pratiques Android:
 * - Repositories et Use Cases: single (instance unique partagée)
 * - ViewModels: viewModel() (instance unique par écran via Jetpack ViewModel)
 */
val appModule = module {

    // ===== HTTP CLIENT =====
    single {
        HttpClient { install(ContentNegotiation) { json(JsonConfig.instance, contentType = Json) } }
    }

    // ===== API & SERVICES =====
    // Single pour les API et services : une seule instance partagée
    single<TokenStore> { InMemoryTokenStore() }

    // API client wired with TokenStore
    single<BackendApi> { KtorbackendApi(get(), get()) }

    // ===== REPOSITORIES =====
    // Single pour les repositories : une seule instance partagée
    single<OnboardingRepository> { OnboardingRepositoryImpl() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<TeacherRepository> { TeacherRepositoryImpl(get()) }
    single<org.julienjnnqin.luxmateapp.domain.repository.ChatRepository> {
        org.julienjnnqin.luxmateapp.data.repository.ChatRepositoryImpl(get())
    }
    single<org.julienjnnqin.luxmateapp.domain.repository.PersonaRepository> {
        org.julienjnnqin.luxmateapp.data.repository.PersonaRepositoryImpl(get())
    }
    single<UserRepository> { UserRepositoryImpl(get()) }


}

// ===== DOMAIN LAYER =====

val domainModule = module {
    // ===== USE CASES =====
    // factoryOf pour les use cases : une nouvelle instance à chaque injection (stateless, pas besoin de partager)
    factoryOf(::CheckOnboardingCompletedUseCase)
    factoryOf(::SetOnboardingCompletedUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::GetAllTeachersUseCase)
    factoryOf(::SearchTeachersUseCase)
    factoryOf(::GetUserProfileUseCase)
    factoryOf(::GetChatHistoryUseCase)
}

// ===== VIEW MODELS =====
// viewModel() pour les ViewModels : gestion du cycle de vie par Compose
// Une instance par écran, détruite quand on quitte l'écran
val viewModelModule = module {
    factoryOf(::AppViewModel)
    factoryOf(::OnboardingViewModel)
    factoryOf(::LoginViewModel)
    factoryOf(::PersonasViewModel)
    factoryOf(::ProfileViewModel)
    factoryOf(::PersonasViewModel)
    factory { (personaId: String) ->
        ChatViewModel(personaId, get())
    }
}