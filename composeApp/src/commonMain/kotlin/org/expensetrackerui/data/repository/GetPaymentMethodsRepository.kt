package org.expensetrackerui.data.repository

import org.expensetrackerui.data.model.PaymentMethod

interface GetPaymentMethodsRepository {
    fun invoke(): List<PaymentMethod>
}