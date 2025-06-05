package org.expensetrackerui.domain.usecase

import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.repository.ExpenseRepository

class SaveExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) {
        // Here you can add any business rules before saving
        // For example, validation beyond UI input validation
        expenseRepository.saveExpense(expense)
    }
}