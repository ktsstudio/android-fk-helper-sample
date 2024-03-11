package ru.ktsstudio.fkext.sample.domain

interface FinanceRepository {

    suspend fun insert()

    suspend fun update()

    suspend fun delete()
}