package ru.ktsstudio.fkext.sample.data.database.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.ktsstudio.fkext.sample.data.database.models.LocalBill
import ru.ktsstudio.fkext.sample.data.database.models.LocalCategory
import ru.ktsstudio.fkext.sample.data.database.models.LocalTransaction
import ru.ktsstudio.fkext.sample.utils.db.base.Relations

data class TransactionWithRelations(

    @Embedded override val embeddedValue: LocalTransaction,

    @Relation(
        entity = LocalBill::class,
        entityColumn = LocalBill.COLUMN_ID,
        parentColumn = LocalTransaction.COLUMN_BILL_ID
    )
    val bills: List<LocalBill>,

    @Relation(
        entity = LocalCategory::class,
        entityColumn = LocalCategory.COLUMN_ID,
        parentColumn = LocalTransaction.COLUMN_CATEGORY_ID
    )
    val categories: List<LocalCategory>,

) : Relations<LocalTransaction> {

    override fun embeddedConstraintsEmpty() = bills.isEmpty() && categories.isEmpty()

    override fun getEmbeddedConstraints() = bills + categories
}
