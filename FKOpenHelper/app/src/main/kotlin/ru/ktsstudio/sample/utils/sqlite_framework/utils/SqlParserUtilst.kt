package ru.ktsstudio.sample.utils.sqlite_framework.utils

import android.util.Log
import timber.log.Timber
import java.util.Locale

private const val WHERE_KEYWORD = " where "
private const val FROM_KEYWORD = " from "
private const val INTO_KEYWORD = " into "
private const val SET_KEYWORD = " set "

internal val String.isInsert: Boolean
    get() = startsWith("insert", ignoreCase = true)

internal val String.isUpdate: Boolean
    get() = startsWith("update", ignoreCase = true) && WHERE_KEYWORD in this

internal val String.isDelete: Boolean
    get() = startsWith("delete", ignoreCase = true) && WHERE_KEYWORD in this

internal val String.updateColumnArgsSize: Int
    get() = substringBefore(WHERE_KEYWORD).count { it == '?' }

internal fun getTableNameFromSqlQuery(sql: String): String? {
    return when {
        sql.isInsert -> getTableNameFromInsertQuery(sql)
        sql.isUpdate -> getTableNameFromUpdateQuery(sql)
        sql.isDelete -> getTableNameFromSelectOrDeleteQuery(sql)
        else -> null
    }
}

private fun getTableNameFromInsertQuery(sql: String): String {
    return prepareSql(sql)
        .lowercase()
        .substringAfter(INTO_KEYWORD)
        .substringBefore(" (")
        .replace("`", "")
        .trim()
}

private fun getTableNameFromUpdateQuery(sql: String): String {
    return prepareSql(sql)
        .lowercase()
        .substringBefore(SET_KEYWORD)
        .replace("`", "")
        .split(" ")
        .last()
        .trim()
}

private fun getTableNameFromSelectOrDeleteQuery(sql: String): String {
    return prepareSql(sql)
        .lowercase()
        .substringAfter(FROM_KEYWORD)
        .substringBefore(WHERE_KEYWORD)
        .replace("`", "")
        .trim()
}

internal fun getColumnNamesFromSqlInsertQuery(sql: String): List<String> {
    return sql.trim()
        .lowercase(Locale.ROOT)
        .substringAfter("(")
        .substringBefore(")")
        .replace("`", "")
        .split(",")
        .map { it.trim() }
}

internal fun getColumnNamesFromSqlUpdateQuery(sql: String): List<String> {
    return sql.trim()
        .lowercase(Locale.ROOT)
        .substringAfter(SET_KEYWORD)
        .substringBefore(WHERE_KEYWORD)
        .replace(Regex("[?`=]"), "")
        .split(",")
        .map { it.trim() }
}

@Suppress("ReturnCount")
internal fun getValuesFromUpdateOrInsertSqlQueryByColumnName(
    sql: String,
    args: List<String>,
    columnName: String,
    getColumnNamesFromSql: (String) -> List<String>,
    isArgsSizeEqualColumnsSize: (List<String>) -> Boolean
): String? {
    if (sql.isUpdate.not() && sql.isInsert.not()) return null

    val columnNameFormatted = columnName.lowercase().trim()
    val columns = getColumnNamesFromSql(sql)

    val index = columns.indexOf(columnNameFormatted)

    if (index == -1 || isArgsSizeEqualColumnsSize(columns).not()) {
        Timber.e(
            "Could not get the value from sql=$sql by columnName=$columnName, " +
                "index==$index, isArgsSizeEqualColumnsSize(columns).not()=${isArgsSizeEqualColumnsSize(columns).not()}"
        )
        return null
    }

    val value = args.getOrNull(index)

    return if (value == "null") null else value
}

internal fun getConditionsFromSqlDeleteQuery(sql: String): String {
    return sql.lowercase().substringAfter(WHERE_KEYWORD)
}

internal fun prepareSql(sql: String): String {
    return sql.split("\n")
        .joinToString(" ") { it.trim() }.trim()
        .replace("nullif(?, 0)", "?")
}

@Suppress("SpreadOperator")
internal fun <T> formatSqlQuery(sql: String, args: Array<T>): String {
    return sql.replace("?", "%S").format(*args)
}
