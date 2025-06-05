package org.expensetrackerui.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.repository.ExpenseRepository

class LocalExpenseDataSource : ExpenseRepository {
    private val expensesFlow = MutableStateFlow<List<Expense>>(emptyList())

    override suspend fun saveExpense(expense: Expense) {
        val currentList = expensesFlow.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.id == expense.id }
        if (existingIndex != -1) {
            currentList[existingIndex] = expense
        } else {
            currentList.add(expense)
        }
        expensesFlow.value = currentList
    }

    override fun getExpenses(): Flow<List<Expense>> {
        return expensesFlow
    }
}