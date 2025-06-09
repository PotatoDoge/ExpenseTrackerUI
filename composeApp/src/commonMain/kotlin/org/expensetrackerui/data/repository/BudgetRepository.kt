package org.expensetrackerui.data.repository

import org.expensetrackerui.data.model.BudgetSummary


interface BudgetRepository {
    fun getBudgetSummary(): BudgetSummary
}