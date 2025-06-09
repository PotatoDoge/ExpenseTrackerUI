package org.expensetrackerui.data.repository.impl

import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.repository.ExpenseRepository
import org.expensetrackerui.data.repository.SaveExpenseRepository

class SaveExpenseRepositoryImpl(
    private val expenseRepository: ExpenseRepository
): SaveExpenseRepository {
    override suspend fun invoke(expense: Expense) {
        // Here you can add any business rules before saving
        // For example, validation beyond UI input validation
        expenseRepository.saveExpense(expense)
    }
}