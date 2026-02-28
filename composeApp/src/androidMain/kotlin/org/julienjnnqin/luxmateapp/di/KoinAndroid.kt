package org.julienjnnqin.luxmateapp.di

import android.content.Context
import org.julienjnnqin.luxmateapp.data.auth.TokenStore
import org.julienjnnqin.luxmateapp.data.auth.TokenStoreAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Android-specific Koin initialization. Call this from your Android Application class (instead of
 * the common `initializeKoin()`) to provide a secure platform TokenStore implementation.
 */

fun initializeKoinAndroid(context: Context) {
    val platformModule = module {
        // éviter une résolution au démarrage et récupérer le Context via `get()`
        single<TokenStore>(createdAtStart = false) { TokenStoreAndroid(get()) }
    }

    if (GlobalContext.getOrNull() == null) {
        startKoin {
            androidContext(context)
            modules(appModule, domainModule, viewModelModule, platformModule)
        }
    }
}
