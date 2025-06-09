package org.expensetrackerui.data.repository.impl

import org.expensetrackerui.data.model.ExpenseTag
import org.expensetrackerui.data.repository.GetExpenseTagsRepository

class GetExpenseTagsRepositoryImpl: GetExpenseTagsRepository {
    override fun invoke(): List<ExpenseTag> {
        return ExpenseTag.entries.toList()
    }
}