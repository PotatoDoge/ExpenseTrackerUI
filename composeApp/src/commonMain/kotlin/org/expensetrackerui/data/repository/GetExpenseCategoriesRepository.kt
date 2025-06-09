package org.expensetrackerui.data.repository

import org.expensetrackerui.data.model.ExpenseCategory

interface GetExpenseCategoriesRepository {
    fun invoke(): List<ExpenseCategory>
}