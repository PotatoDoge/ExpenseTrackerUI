package org.expensetrackerui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform