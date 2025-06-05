package org.expensetrackerui.data.repository

import kotlinx.coroutines.flow.Flow
import org.expensetrackerui.data.model.Expense


interface ExpenseRepository {
    suspend fun saveExpense(expense: Expense)
    fun getExpenses(): Flow<List<Expense>> // Example for listing expenses later
    // Add other CRUD operations as needed
}
