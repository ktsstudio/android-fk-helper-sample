package ru.ktsstudio.fkext.sample.utils.db.fk

import ru.ktsstudio.fkext.sample.data.database.FinanceDB
import ru.ktsstudio.fkext.sample.utils.db.base.BaseEntity

suspend inline fun <reified T : BaseEntity<*>> FinanceDB.insertWithFKCheck(
    itemsToInsert: List<T>,
    insertCallback: (items: List<T>) -> List<Long>
): List<Long> {
    if (itemsToInsert.isEmpty()) return emptyList()
    var insertedIds = emptyList<Long>()

    checkFkException(
        tryBlock = {
            insertedIds = insertCallback(itemsToInsert)
        },
        catchFkExceptionBlock = {
            getFkExceptionFormattingOnSave(
                itemsToSave = itemsToInsert,
                action = TableAction.INSERT,
            )
        }
    )
    return insertedIds
}

suspend inline fun <reified T : BaseEntity<*>> FinanceDB.insertOrUpdateWithFKCheck(
    itemsToInsert: List<T>,
    insertCallback: (items: List<T>) -> Unit
) {
    if (itemsToInsert.isEmpty()) return

    checkFkException(
        tryBlock = {
            insertCallback(itemsToInsert)
        },
        catchFkExceptionBlock = {
            getFkExceptionFormattingOnSave(
                itemsToSave = itemsToInsert,
                action = TableAction.INSERT,
            )
        }
    )
}

