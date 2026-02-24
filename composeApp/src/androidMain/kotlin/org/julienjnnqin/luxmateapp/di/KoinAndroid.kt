package org.julienjnnqin.luxmateapp.di

import android.content.Context
import org.julienjnnqin.luxmateapp.data.auth.TokenStore
import org.julienjnnqin.luxmateapp.data.auth.TokenStoreAndroid
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Android-specific Koin initialization. Call this from your Android Application class (instead of
 * the common `initializeKoin()`) to provide a secure platform TokenStore implementation.
 */
fun initializeKoinAndroid(context: Context) {
    val platformModule = module { single<TokenStore> { TokenStoreAndroid(context) } }

    startKoin { modules(appModule, domainModule, viewModelModule, platformModule) }
}
