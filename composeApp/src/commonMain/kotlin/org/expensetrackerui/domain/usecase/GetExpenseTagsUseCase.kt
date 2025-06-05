package org.expensetrackerui.domain.usecase

import org.expensetrackerui.data.model.ExpenseTag

class GetExpenseTagsUseCase {
    operator fun invoke(): List<ExpenseTag> {
        return ExpenseTag.entries.toList()
    }
}