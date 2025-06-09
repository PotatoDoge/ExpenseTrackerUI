package org.expensetrackerui.di

import org.expensetrackerui.data.repository.BudgetRepository
import org.expensetrackerui.data.repository.ExpenseRepository
import org.expensetrackerui.data.repository.FinancialTipsRepository
import org.expensetrackerui.data.repository.SpendingRepository
import org.expensetrackerui.data.repository.TransactionRepository
import org.expensetrackerui.data.repository.impl.BudgetRepositoryImpl
import org.expensetrackerui.data.repository.impl.FinancialTipsRepositoryImpl
import org.expensetrackerui.data.repository.impl.SpendingRepositoryImpl
import org.expensetrackerui.data.repository.impl.TransactionRepositoryImpl
import org.expensetrackerui.data.source.LocalExpenseDataSource
import org.expensetrackerui.data.repository.GetExpenseCategoriesRepository
import org.expensetrackerui.data.repository.GetExpenseTagsRepository
import org.expensetrackerui.data.repository.GetPaymentMethodsRepository
import org.expensetrackerui.data.repository.SaveExpenseRepository // <--- ADD THIS IMPORT
import org.expensetrackerui.data.repository.impl.GetExpenseCategoriesRepositoryImpl
import org.expensetrackerui.data.repository.impl.GetExpenseTagsRepositoryImpl
import org.expensetrackerui.data.repository.impl.GetPaymentMethodsRepositoryImpl
import org.expensetrackerui.data.repository.impl.SaveExpenseRepositoryImpl
import org.expensetrackerui.presentation.MainViewModel
import org.expensetrackerui.presentation.addexpense.AddExpenseViewModel
import org.expensetrackerui.presentation.home.HomeViewModel
import org.koin.dsl.module

val appModule = module {
    single<BudgetRepository> { BudgetRepositoryImpl() }
    single<FinancialTipsRepository> { FinancialTipsRepositoryImpl() }
    single<SpendingRepository> { SpendingRepositoryImpl() }
    single<TransactionRepository> { TransactionRepositoryImpl() }

    single { LocalExpenseDataSource() }
    single<ExpenseRepository> { get<LocalExpenseDataSource>() }

    single<SaveExpenseRepository> { SaveExpenseRepositoryImpl(get()) }
    single<GetExpenseCategoriesRepository> { GetExpenseCategoriesRepositoryImpl() }
    single<GetExpenseTagsRepository> { GetExpenseTagsRepositoryImpl() }
    single<GetPaymentMethodsRepository> { GetPaymentMethodsRepositoryImpl() }

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

    factory {
        AddExpenseViewModel(
            saveExpenseRepository = get(),
            getExpenseCategoriesRepository = get(),
            getExpenseTagsRepository = get(),
            getPaymentMethodsRepository = get()
        )
    }
}