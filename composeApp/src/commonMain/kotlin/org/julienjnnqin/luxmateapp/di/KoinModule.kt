package org.julienjnnqin.luxmateapp.di

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.julienjnnqin.luxmateapp.data.config.JsonConfig
import org.julienjnnqin.luxmateapp.data.model.RefreshTokenRequest
import org.julienjnnqin.luxmateapp.data.model.TokenResponse
import org.julienjnnqin.luxmateapp.data.remote.BackendApi
import org.julienjnnqin.luxmateapp.data.remote.KtorbackendApi
import org.julienjnnqin.luxmateapp.data.repository.*
import org.julienjnnqin.luxmateapp.domain.repository.*
import org.julienjnnqin.luxmateapp.domain.usecase.*
import org.julienjnnqin.luxmateapp.presentation.AppViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.auth.LoginViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.chat.ChatViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.home.HomeViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.onboarding.OnboardingViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.personas.PersonasViewModel
import org.julienjnnqin.luxmateapp.presentation.screen.profile.ProfileViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Module Koin pour l'injection de dépendances Suit les meilleures pratiques Android:
 * - Repositories et Use Cases: single (instance unique partagée)
 * - ViewModels: viewModel() (instance unique par écran via Jetpack ViewModel)
 */

expect val platformModule: Module

val appModule = module {


    // ===== HTTP CLIENT (UNIFIÉ) =====
    single {
        HttpClient {
            // 1. Gestion du JSON
            install(ContentNegotiation) {
                json(JsonConfig.instance)
            }

            // 2. Gestion automatique des Tokens (le fameux "intercepteur")
            install(Auth) {
                bearer {
                    loadTokens {
                        val settings = get<SettingsRepository>()
                        val access = settings.getAccessToken()
                        val refresh = settings.getRefreshToken()

                        if (access != null && refresh != null) {
                            BearerTokens(access, refresh)
                        } else null
                    }

                    refreshTokens {
                        println("REFRSH: Ktor a détecté une 401 avec challenge, tentative...")
                        val settings = get<SettingsRepository>()
                        val oldRefresh = settings.getRefreshToken() ?: return@refreshTokens null

                        try {

                            // Utilise une instance de HttpClient SANS le plugin Auth pour le refresh
                            // ou assure-toi que l'URL est différente.
                            val response = client.post("https://luxmate.up.railway.app/auth/refresh") {
                                contentType(ContentType.Application.Json)
                                setBody(RefreshTokenRequest(oldRefresh))
                                // On force cette requête à ignorer le plugin Auth pour éviter la boucle
                                markAsRefreshTokenRequest()
                            }.body<TokenResponse>()

                            settings.saveUserToken(response)
                            BearerTokens(response.accessToken, response.refreshToken)
                        } catch (e: Exception) {
                            println("REFRESH FAILED: Nettoyage des tokens... erreur: ${e.message}")
                            settings.cleaUserToken() // ICI : ton AppViewModel va maintenant réagir !
                            null
                        }
                    }
                }
            }
        }
    }

    // ===== API & SERVICES =====
    // On n'injecte plus que le HttpClient (get()), SettingsService n'est plus utile
    // dans KtorbackendApi si tu utilises le plugin Auth ci-dessus.
    single<BackendApi> { KtorbackendApi(get()) }

    // ===== REPOSITORIES =====
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<TeacherRepository> { TeacherRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    // Chat & Persona Repositories
    single<ChatRepository> {
        ChatRepositoryImpl(get())
    }
    single<PersonaRepository> {
        PersonaRepositoryImpl(get())
    }
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
    factoryOf(::IsUserLoggedInUseCase)
    factoryOf(::GetMessagesUseCase)
    factoryOf(::GetSessionsUseCase)
    factoryOf(::GetSessionUseCase)
    factoryOf(::SendMessageUseCase)
    factoryOf(::LoginWithGoogleUseCase)
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
    factoryOf(::HomeViewModel)
    factoryOf(::ChatViewModel)

}