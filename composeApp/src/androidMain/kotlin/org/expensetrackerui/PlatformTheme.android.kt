package org.expensetrackerui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
actual fun isSystemInDarkThemeKmp(): Boolean {
    return isSystemInDarkTheme()
}