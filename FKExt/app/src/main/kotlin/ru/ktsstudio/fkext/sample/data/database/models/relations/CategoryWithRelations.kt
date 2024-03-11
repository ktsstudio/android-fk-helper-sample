package ru.ktsstudio.fkext.sample.data.database.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.ktsstudio.fkext.sample.data.database.models.LocalCategory
import ru.ktsstudio.fkext.sample.data.database.models.LocalTransaction
import ru.ktsstudio.fkext.sample.utils.db.base.Relations

data class CategoryWithRelations(

    @Embedded override val embeddedValue: LocalCategory,

    @Relation(
        entity = LocalTransaction::class,
        entityColumn = LocalTransaction.COLUMN_CATEGORY_ID,
        parentColumn = LocalCategory.COLUMN_ID
    )
    val transactions: List<LocalTransaction>

) : Relations<LocalCategory> {

    override fun embeddedConstraintsEmpty() = transactions.isEmpty()

    override fun getEmbeddedConstraints() = transactions
}
