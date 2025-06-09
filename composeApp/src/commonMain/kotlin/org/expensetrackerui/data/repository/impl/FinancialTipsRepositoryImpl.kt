package org.expensetrackerui.data.repository.impl

import expensetrackerui.composeapp.generated.resources.Res
import expensetrackerui.composeapp.generated.resources.tip_ahorro
import expensetrackerui.composeapp.generated.resources.tip_gastos
import expensetrackerui.composeapp.generated.resources.tip_inversion
import expensetrackerui.composeapp.generated.resources.tip_presupuesto
import org.expensetrackerui.data.model.FinancialTip
import org.expensetrackerui.data.repository.FinancialTipsRepository

class FinancialTipsRepositoryImpl : FinancialTipsRepository {
    override fun getFinancialTips(): List<FinancialTip> {
        return listOf(
            FinancialTip("Ahorra a tiempo", "Consejos para el supermercado.", Res.drawable.tip_ahorro),
            FinancialTip("Haz un presupuesto", "Guía paso a paso.", Res.drawable.tip_presupuesto),
            FinancialTip("Invierte Inteligente", "Primeros pasos.", Res.drawable.tip_inversion),
            FinancialTip("Reduce Gastos", "Identifica fugas de dinero.", Res.drawable.tip_gastos)
        )
    }
}