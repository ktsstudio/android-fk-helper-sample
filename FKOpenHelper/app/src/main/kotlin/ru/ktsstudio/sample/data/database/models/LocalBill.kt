package ru.ktsstudio.sample.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
) {
    companion object {

        const val TABLE_NAME = "bills"

        const val COLUMN_ID = "id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_BALANCE = "balance"
    }
}