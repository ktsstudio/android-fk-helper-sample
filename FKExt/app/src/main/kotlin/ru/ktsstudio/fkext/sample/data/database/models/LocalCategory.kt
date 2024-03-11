package ru.ktsstudio.fkext.sample.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.ktsstudio.fkext.sample.utils.db.base.BaseEntity

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

): BaseEntity<Long> {

    override val tableName: String
        get() = TABLE_NAME

    override val remotePrimaryKeyName: String
        get() = INFO_ITEMS_NAME

    override val remotePrimaryItem: Long
        get() = id

    companion object {
        const val INFO_ITEMS_NAME = "ids"

        const val TABLE_NAME = "category"

        const val COLUMN_ID = "id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_NAME = "name"

        const val QUERY_ALL = "SELECT * FROM $TABLE_NAME"

        const val QUERY_BY_IDS = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID IN (:ids)"
    }
}

