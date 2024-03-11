package ru.ktsstudio.sample.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.ktsstudio.sample.data.database.converter.DecimalConverter
import ru.ktsstudio.sample.data.database.converter.TransactionTypeConverter
import ru.ktsstudio.sample.data.database.dao.BillDao
import ru.ktsstudio.sample.data.database.dao.CategoryDao
import ru.ktsstudio.sample.data.database.dao.TransactionDao
import ru.ktsstudio.sample.data.database.dao.UserDao
import ru.ktsstudio.sample.data.database.models.LocalBill
import ru.ktsstudio.sample.data.database.models.LocalCategory
import ru.ktsstudio.sample.data.database.models.LocalTransaction
import ru.ktsstudio.sample.data.database.models.LocalUser

@Database(
    entities = [
        LocalUser::class,
        LocalBill::class,
        LocalCategory::class,
        LocalTransaction::class,
    ],
    version = 1
)
@TypeConverters(
    DecimalConverter::class,
    TransactionTypeConverter::class,
)
abstract class FinanceDB : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun billDao(): BillDao

    abstract fun categoryDao(): CategoryDao

    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DATABASE_NAME = "finance_db"
    }
}
