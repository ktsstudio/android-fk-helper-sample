package ru.ktsstudio.sample.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import ru.ktsstudio.sample.data.database.models.LocalCategory
import ru.ktsstudio.sample.data.database.models.LocalTransaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactions: List<LocalTransaction>)

    @Update
    suspend fun update(transactions: List<LocalTransaction>)
}