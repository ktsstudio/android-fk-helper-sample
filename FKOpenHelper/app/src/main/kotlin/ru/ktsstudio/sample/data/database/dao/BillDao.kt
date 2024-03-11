package ru.ktsstudio.sample.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.ktsstudio.sample.data.database.models.LocalBill

@Dao
interface BillDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bills: List<LocalBill>)
}