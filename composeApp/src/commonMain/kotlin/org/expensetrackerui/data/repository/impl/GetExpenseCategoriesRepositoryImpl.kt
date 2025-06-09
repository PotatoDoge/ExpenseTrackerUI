package org.expensetrackerui.data.repository.impl

import org.expensetrackerui.data.model.ExpenseCategory
import org.expensetrackerui.data.repository.GetExpenseCategoriesRepository

class GetExpenseCategoriesRepositoryImpl: GetExpenseCategoriesRepository {
    override fun invoke(): List<ExpenseCategory> {
        return ExpenseCategory.entries.toList()
    }
}