package ru.ktsstudio.fkext.sample.utils.db.fk

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import ru.ktsstudio.fkext.sample.data.database.FinanceDB
import ru.ktsstudio.fkext.sample.data.database.models.LocalBill
import ru.ktsstudio.fkext.sample.data.database.models.LocalCategory
import ru.ktsstudio.fkext.sample.data.database.models.LocalTransaction
import ru.ktsstudio.fkext.sample.data.database.models.LocalUser
import ru.ktsstudio.fkext.sample.utils.db.base.BaseEntity
import timber.log.Timber
import java.util.Locale

const val FOREIGN_KEY_EXCEPTION_TEXT = "foreign key constraint failed"

suspend inline fun <reified T : BaseEntity<*>> FinanceDB.getFkExceptionFormattingOnSave(
    itemsToSave: List<T>,
    action: TableAction,
): FkExceptionFormattingInfo<T> {
    val entityInfo = itemsToSave.first()
    val entityInfoToFkIds = mutableMapOf<T, List<Any>>()
    val foreignKeyRelations = getForeignKeyRelationFor(entityInfo)

    Log.d("CHECK_LOG", "foreignKeyRelations = " + foreignKeyRelations)

    Log.d("CHECK_LOG", "itemsToSave = " + entityInfo)

    val errorItems = itemsToSave.filter { itemToInsert ->
        foreignKeyRelations.all { fkRelation ->
            Log.d("CHECK_LOG", "---")
            Log.d("CHECK_LOG", "fkRelation = " + fkRelation)

            val foreignKeyValue = fkRelation.childFkIdExtractor(itemToInsert) ?: return@all true

            Log.d("CHECK_LOG", "foreignKeyValue = " + foreignKeyValue + ", fkRelation.parentFkIds=${fkRelation.parentFkIds}")

            fkRelation.parentFkIds.contains(foreignKeyValue).not()
                .also { constraintFails ->
                    if (constraintFails) {
                        val constraintFailsList = entityInfoToFkIds[fkRelation.entityInfo]
                            ?.toMutableList()
                            ?.apply {
                                add(foreignKeyValue)
                            } ?: listOf(foreignKeyValue)
                        entityInfoToFkIds[fkRelation.entityInfo] = constraintFailsList
                    }
                }
        }
    }

    return FkExceptionFormattingInfo(
        tableName = entityInfo.tableName,
        action = action,
        errorItems = errorItems,
        entityInfoToFkIds = entityInfoToFkIds,
    )
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T : BaseEntity<*>> FinanceDB.getItemsByIds(ids: List<Long>): List<T> {
    return when (T::class) {
        LocalBill::class -> billDao().getByIds(ids) as List<T>
        LocalCategory::class -> categoryDao().getByIds(ids) as List<T>
        LocalTransaction::class -> transactionDao().getByIds(ids) as List<T>
        LocalUser::class -> userDao().getByIds(ids) as List<T>
        else -> error("Can't find entity relation for ${T::class.simpleName}")
    }
}

suspend inline fun <reified T : BaseEntity<*>> FinanceDB.getForeignKeyRelationFor(
    entityInfo: T
): List<ForeignKeyRelation<T>> {
    return when (T::class) {
        LocalBill::class -> listOf(
            ForeignKeyRelation(
                entityInfo = entityInfo,
                parentFkIds = userDao().getAll().map { it.id },
                childFkIdExtractor = { item ->
                    (item as LocalBill).userId
                }
            )
        )
        LocalTransaction::class -> listOf(
            ForeignKeyRelation(
                entityInfo = entityInfo,
                parentFkIds = billDao().getAll().map { it.id },
                childFkIdExtractor = { item ->
                    (item as LocalTransaction).billId
                }
            ),
            ForeignKeyRelation(
                entityInfo = entityInfo,
                parentFkIds = categoryDao().getAll().map { it.id },
                childFkIdExtractor = { item ->
                    (item as LocalTransaction).categoryId
                }
            ),
        )
        else -> error("Can't find foreign key relation for ${T::class.simpleName}")
    }
}

data class ForeignKeyRelation<T>(
    val entityInfo: T,
    val parentFkIds: List<Any>,
    val childFkIdExtractor: (T) -> Any?
)

enum class TableAction {
    INSERT,
    UPDATE,
    DELETE
}

data class FkExceptionFormattingInfo<T : BaseEntity<*>>(
    val tableName: String,
    val action: TableAction,
    val errorItems: List<Any>,
    val entityInfoToFkIds: Map<T, List<Any>>,
)

inline fun <T : BaseEntity<*>> checkFkException(
    tryBlock: () -> Unit,
    catchFkExceptionBlock: () -> FkExceptionFormattingInfo<T>
) {
    try {
        tryBlock()
    } catch (sqliteException: SQLiteConstraintException) {
        sqliteException.message
            ?.lowercase(Locale.getDefault())
            ?.takeIf { errorMessage -> FOREIGN_KEY_EXCEPTION_TEXT in errorMessage }
            ?: throw sqliteException

        val errorMessage = createFkExceptionErrorMessage(catchFkExceptionBlock())
        throw SQLiteConstraintException(errorMessage)
    }
}

fun <T : BaseEntity<*>> createFkExceptionErrorMessage(
    messageInfo: FkExceptionFormattingInfo<T>
): String {
    val tableNameWithAction = "${messageInfo.action.getText()}: " +
        messageInfo.tableName

    val errorItemsFormatted = "Проблемные сущности:\n${
        messageInfo.errorItems.joinToString(separator = "") { "● $it\n" }
    }\n"
    val constraintFailsTableInfo = messageInfo.entityInfoToFkIds.map {
        "${it.key.tableName}(${it.key.remotePrimaryKeyName}=${it.value})"
    }.joinToString(separator = "") { "▸ $it\n" }

    return "$tableNameWithAction\n" +
        errorItemsFormatted +
        "Нарушена связь с таблицами:\n" +
        constraintFailsTableInfo
}

fun TableAction.getText() = when (this) {
    TableAction.INSERT -> "Вставка в таблицу"
    TableAction.UPDATE -> "Обновление в таблице"
    TableAction.DELETE -> "Удаление из таблицы"
}
