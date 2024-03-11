package ru.ktsstudio.fkext.sample.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.ktsstudio.fkext.sample.data.database.models.LocalTransaction
import ru.ktsstudio.fkext.sample.data.database.models.LocalUser
import ru.ktsstudio.fkext.sample.data.database.models.relations.UserWithRelations

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: List<LocalUser>)

    @Delete
    suspend fun delete(users: List<LocalUser>)

    @Query(LocalUser.QUERY_ALL)
    suspend fun getAll(): List<LocalUser>

    @Query(LocalUser.QUERY_BY_IDS)
    fun getByIds(ids: List<Long>): List<LocalUser>

    @Transaction
    @Query(LocalUser.QUERY_BY_IDS)
    fun getByIdsWithRelations(ids: List<Long>): List<UserWithRelations>
}