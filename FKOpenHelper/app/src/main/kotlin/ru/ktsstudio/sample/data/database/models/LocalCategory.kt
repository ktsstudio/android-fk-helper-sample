package ru.ktsstudio.sample.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = LocalCategory.TABLE_NAME,
)
data class LocalCategory(

    @PrimaryKey
    @ColumnInfo(COLUMN_ID)
    val id: Long,

    @ColumnInfo(COLUMN_TYPE)
    val type: TransactionType,

    @ColumnInfo(COLUMN_NAME)
    val name: String,
) {

    companion object {
        const val TABLE_NAME = "category"

        const val COLUMN_ID = "id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_NAME = "name"
    }
}

