package ru.ktsstudio.sample.domain

class FinanceUseCaseImpl(
    private val financeRepository: FinanceRepository
) : FinanceUseCase {

    override suspend fun insert() = financeRepository.insert()

    override suspend fun update() = financeRepository.update()

    override suspend fun delete() = financeRepository.delete()
}