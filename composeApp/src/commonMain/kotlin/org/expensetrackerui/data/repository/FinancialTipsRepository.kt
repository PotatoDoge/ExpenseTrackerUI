package org.expensetrackerui.data.repository

import org.expensetrackerui.data.model.FinancialTip

interface FinancialTipsRepository {
    fun getFinancialTips(): List<FinancialTip>
}