package org.julienjnnqin.luxmateapp.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.julienjnnqin.luxmateapp.data.auth.GoogleAuthManager
import org.julienjnnqin.luxmateapp.domain.auth.SocialAuthManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Android-specific Koin initialization. Call this from your Android Application class (instead of
 * the common `initializeKoin()`) to provide a secure platform TokenStore implementation.
 */
// platformModule.android.kt

actual val platformModule = module {
    single<Settings> {
        SharedPreferencesSettings(get<Context>().getSharedPreferences("luxmate_prefs", 0))
    }
    single<SocialAuthManager> { GoogleAuthManager(get()) }
}

fun initializeKoinAndroid(context: Context) {
    if (GlobalContext.getOrNull() == null) {
        startKoin {
            androidContext(context)
            // On utilise la variable actual définie juste au-dessus
            modules(appModule, domainModule, viewModelModule, platformModule)
        }
    }
}