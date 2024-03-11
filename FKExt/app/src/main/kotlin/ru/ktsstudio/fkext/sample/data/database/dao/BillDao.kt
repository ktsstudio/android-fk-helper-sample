package ru.ktsstudio.fkext.sample.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.ktsstudio.fkext.sample.data.database.models.LocalBill
import ru.ktsstudio.fkext.sample.data.database.models.LocalUser
import ru.ktsstudio.fkext.sample.data.database.models.relations.BillWithRelations
import ru.ktsstudio.fkext.sample.data.database.models.relations.UserWithRelations

@Dao
interface BillDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bills: List<LocalBill>)

    @Query(LocalBill.QUERY_ALL)
    suspend fun getAll(): List<LocalBill>

    @Query(LocalBill.QUERY_BY_IDS)
    fun getByIds(ids: List<Long>): List<LocalBill>

    @Transaction
    @Query(LocalBill.QUERY_BY_IDS)
    fun getByIdsWithRelations(ids: List<Long>): List<BillWithRelations>
}