package org.expensetrackerui.util

import java.text.NumberFormat // Part of Java standard library, available on Android
import java.util.Locale     // Part of Java standard library, available on Android

actual object CurrencyFormatter {
    actual fun formatAmount(amount: Double): String {
        // Use NumberFormat for robust, locale-aware currency formatting
        // For '$X.XX' format, Locale.US is a good starting point.
        // You might want to use Locale.getDefault() or pass a locale for true localization.
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2
        return formatter.format(amount)
    }
}