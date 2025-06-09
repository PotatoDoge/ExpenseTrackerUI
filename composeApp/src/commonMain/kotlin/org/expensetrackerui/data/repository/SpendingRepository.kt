package org.expensetrackerui.data.repository

import org.expensetrackerui.data.model.CategorySpending
import org.expensetrackerui.data.model.PaymentMethodSpending
import org.expensetrackerui.data.model.SpendingItem


interface SpendingRepository {
    fun getPaymentMethodSpending(): List<PaymentMethodSpending>
    fun getCategorySpending(): List<CategorySpending>
    fun getMappedPaymentMethodSpending(): List<SpendingItem>
    fun getMappedCategorySpending(): List<SpendingItem>
}