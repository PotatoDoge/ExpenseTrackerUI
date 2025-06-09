package org.expensetrackerui.data.repository

import org.expensetrackerui.data.model.Transaction

interface TransactionRepository {
    fun getRecentTransactions(): List<Transaction>
}