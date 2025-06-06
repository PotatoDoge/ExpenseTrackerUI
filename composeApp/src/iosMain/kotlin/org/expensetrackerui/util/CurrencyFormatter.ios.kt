package org.expensetrackerui.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.currentLocale

actual object CurrencyFormatter {
    actual fun formatAmount(amount: Double): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterCurrencyStyle
            locale = locale()
            // Ensure 'u' suffix for unsigned integers
            minimumFractionDigits = 2u // <--- POTENTIAL FIX: Ensure 'u' suffix
            maximumFractionDigits = 2u // <--- POTENTIAL FIX: Ensure 'u' suffix

            // If you're getting an error on currencySymbol, ensure it's a String
            // currencySymbol = "$" // Example: if you wanted to force a symbol
        }

        // The stringFromNumber method returns an optional (can be null).
        // It's good practice to provide a fallback, as you have.
        // The error could also be here if NSNumber isn't recognized, but less likely.
        return formatter.stringFromNumber(NSNumber(double = amount)) ?: "Error Formatting"
    }
}