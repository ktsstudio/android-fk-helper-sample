package ru.ktsstudio.fkext.sample.data.database.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.ktsstudio.fkext.sample.data.database.models.LocalBill
import ru.ktsstudio.fkext.sample.data.database.models.LocalUser
import ru.ktsstudio.fkext.sample.utils.db.base.Relations

data class UserWithRelations(

    @Embedded override val embeddedValue: LocalUser,

    @Relation(
        entity = LocalBill::class,
        entityColumn = LocalBill.COLUMN_USER_ID,
        parentColumn = LocalUser.COLUMN_ID
    )
    val bills: List<LocalBill>

) : Relations<LocalUser> {

    override fun embeddedConstraintsEmpty() = bills.isEmpty()

    override fun getEmbeddedConstraints() = bills
}
