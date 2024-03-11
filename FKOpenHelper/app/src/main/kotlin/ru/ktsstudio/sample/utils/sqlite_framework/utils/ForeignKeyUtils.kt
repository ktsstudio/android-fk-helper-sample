package ru.ktsstudio.sample.utils.sqlite_framework.utils

import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.ktsstudio.sample.utils.sqlite_framework.models.ForeignKey
import ru.ktsstudio.sample.utils.sqlite_framework.models.ForeignKeyDelete
import ru.ktsstudio.sample.utils.sqlite_framework.models.ForeignKeyInsertUpdate
import timber.log.Timber

@Suppress("SpreadOperator")
internal inline fun <T, R> SupportSQLiteDatabase.withForeignKeyCheck(
    sql: String,
    args: Array<T>,
    query: (String, Array<T>) -> R
): R {
    try {
        return query(sql, args)
    } catch (e: SQLiteConstraintException) {

        val prepareSql = prepareSql(sql)
        val argsList = args.map { it.toString() }

        val formattedSqlQuery = prepareSql.replace("?", "%S").format(*args)

        val foreignKeyMessage = try {
            getForeignKeyMessage(sql = prepareSql.lowercase(), args = argsList)
        } catch (t: Throwable) {
            Timber.e("Fet foreign key message for sql=$formattedSqlQuery")
            ""
        }

        throw SQLiteConstraintException("$formattedSqlQuery\n$foreignKeyMessage")
    }
}

internal inline fun <T> SupportSQLiteDatabase.withForeignKeyCheck(sql: String, query: (String) -> T): T {
    try {
        return query(sql)
    } catch (e: SQLiteConstraintException) {
        val prepareSql = prepareSql(sql)
        val foreignKeyListMessage = getForeignKeyListMessage(prepareSql)
        throw SQLiteConstraintException("sql=$prepareSql".plus(foreignKeyListMessage))
    }
}

internal inline fun <R> SupportSQLiteDatabase.insertWithForeignKeyCheck(
    table: String,
    contentValues: ContentValues,
    query: () -> R
): R {
    val keys = contentValues.keySet().joinToString(", ")
    val values = contentValues.valueSet().map { it.value }.joinToString(", ")
    val sql = "INSERT INTO $table ($keys) VALUES ($values)"

    return withForeignKeyCheck(sql) { query() }
}

private fun SupportSQLiteDatabase.getForeignKeyListMessage(sql: String): String {
    val foreignKeyList = getTableNameFromSqlQuery(sql)
        ?.let(::queryForeignKeyList)
        ?.joinToString(", ") { it.toString() }

    if (foreignKeyList.isNullOrEmpty()) return ""

    return ", foreignKeyList=$foreignKeyList"
}

private fun SupportSQLiteDatabase.getForeignKeyMessage(
    sql: String,
    args: List<String>,
): String {
    return when {
        sql.isInsert || sql.isUpdate -> {
            val tableName = getTableNameFromSqlQuery(sql) ?: return ""

            getForeignKeyValueForInsertOrUpdate(
                sql = sql,
                args = args,
                tableName = tableName,
            )?.joinToString(",\n") { (foreignKey, value) ->
                "FK Error ($tableName.${foreignKey.localColumn} -> " +
                    "${foreignKey.foreignTable}.${foreignKey.foreignColumn})\n" +
                    "There is no field with ${foreignKey.foreignColumn}=${value} " +
                    "in the ${foreignKey.foreignTable} table"
            }.orEmpty()
        }

        sql.isDelete -> {
            val tableName = getTableNameFromSqlQuery(sql) ?: return ""

            getForeignKeyValuesForDelete(
                sql = sql,
                args = args,
                tableName = tableName,
            )?.joinToString(",\n") { (foreignKey, primaryKeyName, primaryKeyValue, value) ->
                "FK Error ($value.${foreignKey?.localColumn} -> " +
                    "${foreignKey?.foreignTable}.${foreignKey?.foreignColumn})\n" +
                    "For $tableName.$primaryKeyName=$primaryKeyValue: it is not " +
                    "possible to delete the field, because the " +
                    "${foreignKey?.foreignTable}.${foreignKey?.foreignColumn} " +
                    "is used in the table \"$value\""
            }.orEmpty()
        }

        else -> ""
    }
}

private fun SupportSQLiteDatabase.getForeignKeyValueForInsertOrUpdate(
    sql: String,
    args: List<String>,
    tableName: String,
): List<ForeignKeyInsertUpdate>? {
    val foreignKeyList = queryForeignKeyList(tableName)
        .ifEmpty {
            return null
        }

    return foreignKeyList.mapNotNull { foreignKey ->
        val value = getValuesFromUpdateOrInsertSqlQueryByColumnName(sql, args, foreignKey)

        value?.let {
            val cursor = query(
                "SELECT * FROM ${foreignKey.foreignTable} WHERE ${foreignKey.foreignColumn} = ?",
                arrayOf(value)
            )

            val isForeignKey = cursor.use { it.moveToFirst() }.not()

            if (isForeignKey) {
                ForeignKeyInsertUpdate(
                    foreignKey = foreignKey,
                    value = value,
                )
            } else {
                null
            }
        }
    }.ifEmpty { null }
}

private fun getValuesFromUpdateOrInsertSqlQueryByColumnName(
    sql: String,
    args: List<String>,
    foreignKey: ForeignKey
) = when {
    sql.isInsert -> getValuesFromUpdateOrInsertSqlQueryByColumnName(
        sql = sql,
        args = args,
        columnName = foreignKey.localColumn,
        getColumnNamesFromSql = ::getColumnNamesFromSqlInsertQuery,
        isArgsSizeEqualColumnsSize = { columns -> args.size == columns.size }
    )

    sql.isUpdate -> getValuesFromUpdateOrInsertSqlQueryByColumnName(
        sql = sql,
        args = args,
        columnName = foreignKey.localColumn,
        getColumnNamesFromSql = ::getColumnNamesFromSqlUpdateQuery,
        isArgsSizeEqualColumnsSize = { columns -> sql.updateColumnArgsSize == columns.size }
    )

    else -> null
}

private fun SupportSQLiteDatabase.getForeignKeyValuesForDelete(
    sql: String,
    tableName: String,
    args: List<String>,
): List<ForeignKeyDelete>? {

    val foreignKeyList = findForeignKeysInAllTable(tableName)

    if (sql.isDelete.not() || foreignKeyList.isEmpty()) return null

    val primaryKeyName = queryPrimaryKeyName(tableName) ?: return null

    val sqlWithArgs = formatSqlQuery(sql, args.toTypedArray())
    val conditions = getConditionsFromSqlDeleteQuery(sql = sqlWithArgs)

    val cursorWithDeleteConditions = query("SELECT * FROM $tableName WHERE $conditions")

    val primaryKeyValues = mutableListOf<String>()

    cursorWithDeleteConditions.use {
        while (it.moveToNext()) {
            val primaryKeyIndex = it.getColumnIndex(primaryKeyName)
            if (primaryKeyIndex != -1) {
                primaryKeyValues += it.getString(primaryKeyIndex)
            }
        }
    }

    return primaryKeyValues.mapNotNull { primaryKeyValue ->
        findForeignKeyCreateErrorsForDelete(
            primaryKeyValue = primaryKeyValue,
            foreignKeyList = foreignKeyList,
            primaryKeyName = primaryKeyName,
        )
    }.flatten()
}

private fun SupportSQLiteDatabase.findForeignKeyCreateErrorsForDelete(
    primaryKeyValue: String,
    foreignKeyList: List<ForeignKey>,
    primaryKeyName: String
): List<ForeignKeyDelete>? {
    val result = mutableListOf<ForeignKeyDelete>()

    result += foreignKeyList.mapNotNull { foreignKey ->
        val cursor = query(
            "SELECT * FROM ${foreignKey.localTable} WHERE ${foreignKey.localColumn} = ?",
            arrayOf(primaryKeyValue)
        )

        val isForeignKey = cursor.use { it.moveToFirst() }

        if (isForeignKey) {
            ForeignKeyDelete(
                foreignKey = foreignKey,
                primaryKeyName = primaryKeyName,
                primaryKeyValue = primaryKeyValue,
                useTable = foreignKey.localTable,
            )
        } else {
            null
        }
    }

    return result.ifEmpty { null }
}

