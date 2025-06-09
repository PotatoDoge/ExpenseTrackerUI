package org.expensetrackerui.data.repository

import org.expensetrackerui.data.model.ExpenseTag

interface GetExpenseTagsRepository {
    fun invoke(): List<ExpenseTag>
}