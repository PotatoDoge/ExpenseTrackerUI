package org.expensetrackerui.data.repository.impl

import org.expensetrackerui.data.model.BudgetSummary
import org.expensetrackerui.data.repository.BudgetRepository

class BudgetRepositoryImpl : BudgetRepository {
    override fun getBudgetSummary(): BudgetSummary {
        return BudgetSummary(totalSpent = 1523.56, totalIncome = 1523.00)
    }
}