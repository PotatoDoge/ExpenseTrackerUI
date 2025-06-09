package org.expensetrackerui.data.repository

import org.expensetrackerui.data.model.Expense

interface SaveExpenseRepository {
    suspend fun invoke(expense: Expense)
}