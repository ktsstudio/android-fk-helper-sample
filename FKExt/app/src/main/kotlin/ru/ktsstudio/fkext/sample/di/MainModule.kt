package ru.ktsstudio.fkext.sample.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.ktsstudio.fkext.sample.data.FinanceRepositoryImpl
import ru.ktsstudio.fkext.sample.domain.FinanceRepository
import ru.ktsstudio.fkext.sample.domain.FinanceUseCase
import ru.ktsstudio.fkext.sample.domain.FinanceUseCaseImpl
import ru.ktsstudio.fkext.sample.presentation.FinanceViewModel

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
            db = get(),
            userDao = get(),
            billDao = get(),
            categoryDao = get(),
            transactionDao = get(),
        )
    }
}