package org.julienjnnqin.luxmateapp.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

/**
 * iOS-specific Koin initialization. Call this from your iOS app start (Kotlin/Native) to provide
 * the iOS TokenStore.
 */
fun initializeKoinIos() {
    val platformModule = module {
        //single<TokenStore> { TokenStoreIos() }
        single<Settings> {
            // On passe l'instance native, la lib l'enrobe
            NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
        }
    }

    startKoin { modules(appModule, domainModule, viewModelModule, platformModule) }
}
