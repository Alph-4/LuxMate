package org.julienjnnqin.luxmateapp.di

import org.julienjnnqin.luxmateapp.data.auth.TokenStore
import org.julienjnnqin.luxmateapp.data.auth.TokenStoreIos
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * iOS-specific Koin initialization.
 * Call this from your iOS app start (Kotlin/Native) to provide the iOS TokenStore.
 */
fun initializeKoinIos() {
    val platformModule = module {
        single<TokenStore> { TokenStoreIos() }
    }

    startKoin {
        modules(appModule, domainModule, viewModelModule, platformModule)
    }
}
