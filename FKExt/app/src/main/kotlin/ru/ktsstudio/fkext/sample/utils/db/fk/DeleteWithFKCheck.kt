package ru.ktsstudio.fkext.sample.utils.db.fk

import ru.ktsstudio.fkext.sample.utils.db.batchedQueryInTransaction
import ru.ktsstudio.fkext.sample.utils.db.queryWithParameterCountCheck
import ru.ktsstudio.fkext.sample.data.database.FinanceDB
import ru.ktsstudio.fkext.sample.data.database.models.LocalBill
import ru.ktsstudio.fkext.sample.data.database.models.LocalCategory
import ru.ktsstudio.fkext.sample.data.database.models.LocalTransaction
import ru.ktsstudio.fkext.sample.data.database.models.LocalUser
import ru.ktsstudio.fkext.sample.utils.db.base.BaseEntity
import ru.ktsstudio.fkext.sample.utils.db.base.Relations

@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T : BaseEntity<*>> FinanceDB.deleteWithFKCheck(
    itemIdsToDelete: List<Long>,
    deleteCallback: (items: List<Long>) -> Unit
) {
    if (itemIdsToDelete.isEmpty()) return
    checkFkException(
        tryBlock = {
            batchedQueryInTransaction(
                list = itemIdsToDelete,
                query = deleteCallback
            )
        },
        catchFkExceptionBlock = {
            val items = getItemsByIds<T>(itemIdsToDelete)
            val entityInfo = items.first()
            val entityInfoToFkIds = mutableMapOf<BaseEntity<*>, List<Any>>()
            val entityRelations: List<Relations<T>> = itemIdsToDelete.queryWithParameterCountCheck { ids ->
                getEntityRelationByIds<T>(ids) as List<Relations<T>>
            }

            val errorItems = entityRelations.filter { entityRelation ->
                entityRelation.embeddedConstraintsEmpty().not()
                    .also { constraintFails ->
                        if (constraintFails) {
                            entityInfoToFkIds.putAll(
                                entityRelation.getEmbeddedConstraints()
                                    .map { relation ->
                                        relation to relation.remotePrimaryItem
                                    }
                                    .filter { it.second != null }
                                    .groupBy { it.first }
                                    .mapValues { (_, infoItems) ->
                                        infoItems.mapNotNull { infoItem -> infoItem.second }
                                    }
                            )
                        }
                    }
            }.map { it.embeddedValue }

            FkExceptionFormattingInfo(
                tableName = entityInfo.tableName,
                action = TableAction.DELETE,
                errorItems = errorItems,
                entityInfoToFkIds = entityInfoToFkIds
            )
        }
    )
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T : BaseEntity<*>> FinanceDB.getEntityRelationByIds(
    ids: List<Long>
): List<Any> {
    return when (T::class) {
        LocalBill::class -> billDao().getByIdsWithRelations(ids)
        LocalTransaction::class -> transactionDao().getByIdsWithRelations(ids)
        LocalUser::class -> userDao().getByIdsWithRelations(ids)
        LocalCategory::class -> categoryDao().getByIdsWithRelations(ids)

        else -> error("Can't find entity relation for ${T::class.simpleName}")
    }
}
