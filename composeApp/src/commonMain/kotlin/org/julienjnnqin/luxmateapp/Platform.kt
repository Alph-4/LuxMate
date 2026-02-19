package org.julienjnnqin.luxmateapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform