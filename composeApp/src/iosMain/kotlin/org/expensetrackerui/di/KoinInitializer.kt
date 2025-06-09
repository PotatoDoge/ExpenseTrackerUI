package org.expensetrackerui.di

import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun initKoin() {
    startKoin {
        printLogger(Level.ERROR)
        modules(appModule)
    }
}