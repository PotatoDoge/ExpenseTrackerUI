package org.expensetrackerui.domain.usecase

import org.expensetrackerui.data.model.PaymentMethod

class GetPaymentMethodsUseCase {
    operator fun invoke(): List<PaymentMethod> {
        return PaymentMethod.entries.toList()
    }
}