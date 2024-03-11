package ru.ktsstudio.fkext.sample.data.database.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.ktsstudio.fkext.sample.data.database.models.LocalBill
import ru.ktsstudio.fkext.sample.data.database.models.LocalTransaction
import ru.ktsstudio.fkext.sample.data.database.models.LocalUser
import ru.ktsstudio.fkext.sample.utils.db.base.Relations

data class BillWithRelations(

    @Embedded
    override val embeddedValue: LocalBill,

    @Relation(
        entity = LocalUser::class,
        entityColumn = LocalUser.COLUMN_ID,
        parentColumn = LocalBill.COLUMN_USER_ID
    )
    val users: List<LocalUser>,

    @Relation(
        entity = LocalTransaction::class,
        entityColumn = LocalTransaction.COLUMN_BILL_ID,
        parentColumn = LocalBill.COLUMN_ID
    )
    val transactions: List<LocalTransaction>

) : Relations<LocalBill> {

    override fun embeddedConstraintsEmpty() = users.isEmpty() && transactions.isEmpty()

    override fun getEmbeddedConstraints() = users + transactions
}
