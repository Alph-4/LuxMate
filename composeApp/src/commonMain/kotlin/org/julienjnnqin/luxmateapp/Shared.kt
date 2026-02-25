package org.julienjnnqin.luxmateapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

private val platform = getPlatform()

fun greet(): String {
        return "Hello, ${platform.name}!"
}

expect val myLang:String?
expect val myCountry:String?
   