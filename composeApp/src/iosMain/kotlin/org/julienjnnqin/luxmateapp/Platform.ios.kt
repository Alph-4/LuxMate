package org.julienjnnqin.luxmateapp

import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val myLang: String?
    get() = NSLocale.currentLocale.languageCode

actual val myCountry: String?
    get() = NSLocale.currentLocale.countryCode
   