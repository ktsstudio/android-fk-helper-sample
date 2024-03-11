package ru.ktsstudio.sample.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.ktsstudio.sample.data.database.models.LocalBill
import ru.ktsstudio.sample.data.database.models.LocalCategory

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categories: List<LocalCategory>)
}