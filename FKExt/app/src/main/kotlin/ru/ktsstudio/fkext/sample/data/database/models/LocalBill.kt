package ru.ktsstudio.fkext.sample.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.ktsstudio.fkext.sample.utils.db.base.BaseEntity
import java.math.BigDecimal

@Entity(
    tableName = LocalBill.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalUser::class,
            childColumns = [LocalBill.COLUMN_USER_ID],
            parentColumns = [LocalUser.COLUMN_ID]
        )
    ]
)
data class LocalBill(

    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long,

    @ColumnInfo(name = COLUMN_USER_ID)
    val userId: Long,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = COLUMN_BALANCE)
    val balance: BigDecimal,
) : BaseEntity<Long> {

    override val tableName: String
        get() = TABLE_NAME

    override val remotePrimaryKeyName: String
        get() = INFO_ITEMS_NAME

    override val remotePrimaryItem: Long
        get() = userId

    companion object {

        const val INFO_ITEMS_NAME = "user_id"

        const val TABLE_NAME = "bills"

        const val COLUMN_ID = "id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_BALANCE = "balance"

        const val QUERY_ALL = "SELECT * FROM $TABLE_NAME"
        const val QUERY_BY_IDS = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID IN (:ids)"

    }
}