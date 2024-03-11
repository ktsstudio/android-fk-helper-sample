package ru.ktsstudio.sample.data

import ru.ktsstudio.sample.data.database.dao.BillDao
import ru.ktsstudio.sample.data.database.dao.CategoryDao
import ru.ktsstudio.sample.data.database.dao.TransactionDao
import ru.ktsstudio.sample.data.database.dao.UserDao
import ru.ktsstudio.sample.data.database.models.LocalBill
import ru.ktsstudio.sample.data.database.models.LocalCategory
import ru.ktsstudio.sample.data.database.models.LocalTransaction
import ru.ktsstudio.sample.data.database.models.LocalUser
import ru.ktsstudio.sample.data.database.models.TransactionType
import ru.ktsstudio.sample.domain.FinanceRepository
import ru.ktsstudio.sample.utils.eventbus.EventBus
import java.math.BigDecimal

class FinanceRepositoryImpl(
    private val userDao: UserDao,
    private val billDao: BillDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
) : FinanceRepository {

    override suspend fun insert() {
        try {
            userDao.insert(listOf(LocalUser(id = 1, name = "TEST_USER")))
            billDao.insert(listOf(LocalBill(id = 1, userId = 1, name = "TEST_BILL", balance = BigDecimal.TEN)))
            categoryDao.insert(listOf(LocalCategory(id = 1, type = TransactionType.INCOME, name = "TEST_CATEGORY")))
            transactionDao.insert(
                listOf(
                    LocalTransaction(
                        id = 1,
                        billId = 2,
                        categoryId = 3,
                        amount = BigDecimal.TEN,
                        date = 0,
                        comment = null
                    )
                )
            )
        } catch (t: Throwable) {
            t.message?.let(EventBus::send)
        }
    }

    override suspend fun update() {
        try {
            transactionDao.insert(
                listOf(
                    LocalTransaction(
                        id = 2,
                        billId = 1,
                        categoryId = 1,
                        amount = BigDecimal.TEN,
                        date = 0,
                        comment = null
                    )
                )
            )
            transactionDao.update(
                listOf(
                    LocalTransaction(
                        id = 2,
                        billId = 5,
                        categoryId = 36,
                        amount = BigDecimal.TEN,
                        date = 0,
                        comment = null
                    )
                )
            )
        } catch (t: Throwable) {
            t.message?.let(EventBus::send)
        }
    }

    override suspend fun delete() {
        try {
            userDao.insert(listOf(LocalUser(id = 10, name = "TEST_USER")))
            billDao.insert(listOf(LocalBill(id = 10, userId = 10, name = "TEST_BILL", balance = BigDecimal.TEN)))
            userDao.delete(listOf(LocalUser(id = 10, name = "TEST_USER")))
        } catch (t: Throwable) {
            t.message?.let(EventBus::send)
        }
    }
}