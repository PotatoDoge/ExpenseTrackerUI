package org.expensetrackerui.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Month
import kotlinx.datetime.minus

fun formatDateForDisplay(date: LocalDate): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val yesterday = today.minus(1, DateTimeUnit.DAY)

    return when (date) {
        today -> "Hoy"
        yesterday -> "Ayer"
        else -> {
            val day = date.dayOfMonth
            val monthName = when (date.month) {
                Month.JANUARY -> "enero"
                Month.FEBRUARY -> "febrero"
                Month.MARCH -> "marzo"
                Month.APRIL -> "abril"
                Month.MAY -> "mayo"
                Month.JUNE -> "junio"
                Month.JULY -> "julio"
                Month.AUGUST -> "agosto"
                Month.SEPTEMBER -> "septiembre"
                Month.OCTOBER -> "octubre"
                Month.NOVEMBER -> "noviembre"
                Month.DECEMBER -> "diciembre"
                else -> ""
            }
            val year = date.year
            "$day de $monthName de $year"
        }
    }
}