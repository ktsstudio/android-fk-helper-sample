package ru.ktsstudio.fkext.sample.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.ktsstudio.fkext.sample.data.database.FinanceDB
import ru.ktsstudio.fkext.sample.data.database.dao.BillDao
import ru.ktsstudio.fkext.sample.data.database.dao.CategoryDao
import ru.ktsstudio.fkext.sample.data.database.dao.TransactionDao
import ru.ktsstudio.fkext.sample.data.database.dao.UserDao
import ru.ktsstudio.fkext.sample.data.database.models.LocalBill
import ru.ktsstudio.fkext.sample.data.database.models.LocalCategory
import ru.ktsstudio.fkext.sample.data.database.models.LocalTransaction
import ru.ktsstudio.fkext.sample.data.database.models.LocalUser
import ru.ktsstudio.fkext.sample.data.database.models.TransactionType
import ru.ktsstudio.fkext.sample.domain.FinanceRepository
import ru.ktsstudio.fkext.sample.utils.db.fk.deleteWithFKCheck
import ru.ktsstudio.fkext.sample.utils.db.fk.insertOrUpdateWithFKCheck
import ru.ktsstudio.fkext.sample.utils.db.fk.insertWithFKCheck
import ru.ktsstudio.fkext.sample.utils.db.fk.updateWithFKCheck
import ru.ktsstudio.fkext.sample.utils.db.withTransaction
import ru.ktsstudio.fkext.sample.utils.eventbus.EventBus
import java.math.BigDecimal

class FinanceRepositoryImpl(
    private val db: FinanceDB,
    private val userDao: UserDao,
    private val billDao: BillDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
) : FinanceRepository {

    override suspend fun insert() {
        withContext(Dispatchers.IO) {
            try {
                db.withTransaction {
                    userDao.insert(listOf(LocalUser(id = 1, name = "TEST_USER")))
                    billDao.insert(listOf(LocalBill(id = 1, userId = 1, name = "TEST_BILL", balance = BigDecimal.TEN)))
                    categoryDao.insert(
                        listOf(
                            LocalCategory(
                                id = 1,
                                type = TransactionType.INCOME,
                                name = "TEST_CATEGORY"
                            )
                        )
                    )
                    db.insertWithFKCheck(
                        itemsToInsert = listOf(
                            LocalTransaction(
                                id = 1,
                                billId = 2,
                                categoryId = 3,
                                amount = BigDecimal.TEN,
                                date = 0,
                                comment = null
                            )
                        )
                    ) {
                        transactionDao.insert(it)
                    }
                }
            } catch (t: Throwable) {
                t.message?.let(EventBus::send)
            }
        }
    }

    override suspend fun update() {
        withContext(Dispatchers.IO) {
            try {
                db.withTransaction {
                    userDao.insert(listOf(LocalUser(id = 1, name = "TEST_USER")))
                    billDao.insert(listOf(LocalBill(id = 1, userId = 1, name = "TEST_BILL", balance = BigDecimal.TEN)))
                    categoryDao.insert(
                        listOf(
                            LocalCategory(
                                id = 1,
                                type = TransactionType.INCOME,
                                name = "TEST_CATEGORY"
                            )
                        )
                    )
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
                    db.insertOrUpdateWithFKCheck<LocalTransaction>(
                        itemsToInsert = listOf(
                            LocalTransaction(
                                id = 2,
                                billId = 5,
                                categoryId = 36,
                                amount = BigDecimal.TEN,
                                date = 0,
                                comment = null
                            )
                        ),
                    ) {
                        transactionDao.update(it)
                    }
                }
            } catch (t: Throwable) {
                t.message?.let(EventBus::send)
            }
        }
    }

    override suspend fun delete() {
        withContext(Dispatchers.IO) {
            try {
                db.withTransaction {
                    userDao.insert(listOf(LocalUser(id = 10, name = "TEST_USER")))
                    billDao.insert(
                        listOf(
                            LocalBill(
                                id = 10,
                                userId = 10,
                                name = "TEST_BILL",
                                balance = BigDecimal.TEN
                            )
                        )
                    )

                    db.deleteWithFKCheck<LocalUser>(
                        itemIdsToDelete = listOf(10),
                    ) {
                        userDao.delete(listOf(LocalUser(id = it.first(), name = "TEST_USER")))
                    }
                }
            } catch (t: Throwable) {
                t.message?.let(EventBus::send)
            }
        }
    }
}