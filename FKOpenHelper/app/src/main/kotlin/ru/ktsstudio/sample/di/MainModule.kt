package ru.ktsstudio.sample.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.ktsstudio.sample.data.FinanceRepositoryImpl
import ru.ktsstudio.sample.domain.FinanceRepository
import ru.ktsstudio.sample.domain.FinanceUseCase
import ru.ktsstudio.sample.domain.FinanceUseCaseImpl
import ru.ktsstudio.sample.presentation.FinanceViewModel

val mainModule = module {
    viewModel {
        FinanceViewModel(
            financeUseCase = get()
        )
    }
    factory<FinanceUseCase> {
        FinanceUseCaseImpl(
            financeRepository = get()
        )
    }
    factory<FinanceRepository> {
        FinanceRepositoryImpl(
            userDao = get(),
            billDao = get(),
            categoryDao = get(),
            transactionDao = get(),
        )
    }
}