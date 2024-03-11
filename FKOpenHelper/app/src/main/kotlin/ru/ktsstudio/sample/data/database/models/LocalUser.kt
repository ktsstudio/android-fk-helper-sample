package ru.ktsstudio.sample.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalUser.TABLE_NAME)
data class LocalUser(

    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
) {

    companion object {

        const val TABLE_NAME = "users"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }
}