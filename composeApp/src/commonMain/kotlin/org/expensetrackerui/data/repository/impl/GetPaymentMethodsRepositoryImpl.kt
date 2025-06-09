package org.expensetrackerui.data.repository.impl

import org.expensetrackerui.data.model.PaymentMethod
import org.expensetrackerui.data.repository.GetPaymentMethodsRepository

class GetPaymentMethodsRepositoryImpl: GetPaymentMethodsRepository {
    override fun invoke(): List<PaymentMethod> {
        return PaymentMethod.entries.toList()
    }
}