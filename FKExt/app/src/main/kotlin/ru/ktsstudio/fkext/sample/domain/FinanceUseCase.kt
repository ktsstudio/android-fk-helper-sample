package ru.ktsstudio.fkext.sample.domain

interface FinanceUseCase {

    suspend fun insert()

    suspend fun update()

    suspend fun delete()
}

