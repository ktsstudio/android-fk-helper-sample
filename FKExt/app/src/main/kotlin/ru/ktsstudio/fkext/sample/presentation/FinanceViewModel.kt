package ru.ktsstudio.fkext.sample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ktsstudio.fkext.sample.domain.FinanceUseCase

class FinanceViewModel(
    private val financeUseCase: FinanceUseCase
) : ViewModel() {

    fun insert() {
        viewModelScope.launch {
            financeUseCase.insert()
        }
    }

    fun update() {
        viewModelScope.launch {
            financeUseCase.update()
        }
    }

    fun delete() {
        viewModelScope.launch {
            financeUseCase.delete()
        }
    }
}