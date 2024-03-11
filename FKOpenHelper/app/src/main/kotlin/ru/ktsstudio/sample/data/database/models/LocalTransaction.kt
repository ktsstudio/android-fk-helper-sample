package ru.ktsstudio.sample.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = LocalTransaction.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalBill::class,
            childColumns = [LocalTransaction.COLUMN_BILL_ID],
            parentColumns = [LocalBill.COLUMN_ID]
        ),
        ForeignKey(
            entity = LocalCategory::class,
            childColumns = [LocalTransaction.COLUMN_CATEGORY_ID],
            parentColumns = [LocalCategory.COLUMN_ID]
        ),
    ]
)
data class LocalTransaction(

    @PrimaryKey
    @ColumnInfo(COLUMN_ID)
    val id: Long,

    @ColumnInfo(COLUMN_BILL_ID)
    val billId: Long,

    @ColumnInfo(COLUMN_CATEGORY_ID)
    val categoryId: Long,

    @ColumnInfo(COLUMN_AMOUNT)
    val amount: BigDecimal,

    @ColumnInfo(COLUMN_DATE)
    val date: Long,

    @ColumnInfo(COLUMN_COMMENT)
    val comment: String?,
) {
    companion object {
        const val TABLE_NAME = "transactions"

        const val COLUMN_ID = "id"
        const val COLUMN_BILL_ID = "bill_id"
        const val COLUMN_CATEGORY_ID = "category_id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DATE = "date"
        const val COLUMN_COMMENT = "comment"
    }
}