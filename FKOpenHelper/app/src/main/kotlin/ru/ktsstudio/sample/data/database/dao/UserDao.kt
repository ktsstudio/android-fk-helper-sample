package ru.ktsstudio.sample.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.ktsstudio.sample.data.database.models.LocalUser

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: List<LocalUser>)

    @Delete
    suspend fun delete(users: List<LocalUser>)
}