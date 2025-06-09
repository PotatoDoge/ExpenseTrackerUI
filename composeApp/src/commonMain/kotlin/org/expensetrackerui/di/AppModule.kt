package org.expensetrackerui.di

import org.expensetrackerui.data.repository.BudgetRepository
import org.expensetrackerui.data.repository.FinancialTipsRepository
import org.expensetrackerui.data.repository.SpendingRepository
import org.expensetrackerui.data.repository.TransactionRepository
import org.expensetrackerui.data.repository.impl.BudgetRepositoryImpl
import org.expensetrackerui.data.repository.impl.FinancialTipsRepositoryImpl
import org.expensetrackerui.data.repository.impl.SpendingRepositoryImpl
import org.expensetrackerui.data.repository.impl.TransactionRepositoryImpl
import org.expensetrackerui.presentation.MainViewModel
import org.expensetrackerui.presentation.home.HomeViewModel
import org.koin.dsl.module

val appModule = module {
    single<BudgetRepository> { BudgetRepositoryImpl() }
    single<FinancialTipsRepository> { FinancialTipsRepositoryImpl() }
    single<SpendingRepository> { SpendingRepositoryImpl() }
    single<TransactionRepository> { TransactionRepositoryImpl() }

    factory {
        HomeViewModel(
            budgetRepository = get(),
            spendingRepository = get(),
            transactionRepository = get(),
            financialTipsRepository = get()
        )
    }

    factory {
        MainViewModel()
    }
}