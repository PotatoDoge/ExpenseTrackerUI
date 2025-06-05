package org.expensetrackerui.domain.usecase

import org.expensetrackerui.data.model.ExpenseCategory

class GetExpenseCategoriesUseCase {
    operator fun invoke(): List<ExpenseCategory> {
        return ExpenseCategory.entries.toList()
    }
}