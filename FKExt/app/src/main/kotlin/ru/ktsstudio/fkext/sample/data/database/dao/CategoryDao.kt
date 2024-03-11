package ru.ktsstudio.fkext.sample.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.ktsstudio.fkext.sample.data.database.models.LocalBill
import ru.ktsstudio.fkext.sample.data.database.models.LocalCategory
import ru.ktsstudio.fkext.sample.data.database.models.relations.BillWithRelations
import ru.ktsstudio.fkext.sample.data.database.models.relations.CategoryWithRelations

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categories: List<LocalCategory>)


    @Query(LocalCategory.QUERY_ALL)
    suspend fun getAll(): List<LocalCategory>

    @Query(LocalCategory.QUERY_BY_IDS)
    fun getByIds(ids: List<Long>): List<LocalCategory>


    @Transaction
    @Query(LocalCategory.QUERY_BY_IDS)
    fun getByIdsWithRelations(ids: List<Long>): List<CategoryWithRelations>
}