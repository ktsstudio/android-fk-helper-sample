package ru.ktsstudio.fkext.sample.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ru.ktsstudio.fkext.sample.data.database.models.LocalCategory
import ru.ktsstudio.fkext.sample.data.database.models.LocalTransaction
import ru.ktsstudio.fkext.sample.data.database.models.LocalUser
import ru.ktsstudio.fkext.sample.data.database.models.relations.TransactionWithRelations
import ru.ktsstudio.fkext.sample.data.database.models.relations.UserWithRelations

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactions: List<LocalTransaction>): List<Long>

    @Update
    suspend fun update(transactions: List<LocalTransaction>)

    @Query(LocalTransaction.QUERY_ALL)
    suspend fun getAll(): List<LocalTransaction>

    @Query(LocalTransaction.QUERY_BY_IDS)
    fun getByIds(ids: List<Long>): List<LocalTransaction>

    @Transaction
    @Query(LocalTransaction.QUERY_BY_IDS)
    fun getByIdsWithRelations(ids: List<Long>): List<TransactionWithRelations>
}