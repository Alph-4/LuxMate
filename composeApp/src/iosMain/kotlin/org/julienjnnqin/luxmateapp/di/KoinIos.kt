package org.julienjnnqin.luxmateapp.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.julienjnnqin.luxmateapp.data.auth.IosGoogleAuthManager
import org.julienjnnqin.luxmateapp.domain.auth.SocialAuthManager
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

/**
 * iOS-specific Koin initialization. Call this from your iOS app start (Kotlin/Native) to provide
 * the iOS TokenStore.
 */

actual val platformModule = module {
    //single<TokenStore> { TokenStoreIos() }
    single<Settings> {
        // On passe l'instance native, la lib l'enrobe
        NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
    }
    single<SocialAuthManager> { IosGoogleAuthManager() }
}

fun initializeKoinIos() {
    startKoin { modules(appModule, domainModule, viewModelModule, platformModule) }
}
