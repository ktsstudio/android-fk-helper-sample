package ru.ktsstudio.sample.utils.sqlite_framework

import android.content.ContentValues
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteStatement
import ru.ktsstudio.sample.utils.sqlite_framework.utils.insertWithForeignKeyCheck
import ru.ktsstudio.sample.utils.sqlite_framework.utils.withForeignKeyCheck

internal class InterceptorSQLiteDatabase(
    private val database: SupportSQLiteDatabase
) : SupportSQLiteDatabase by database {

    override fun compileStatement(sql: String): SupportSQLiteStatement {
        return InterceptorSQLiteStatement(
            supportSQLiteStatement = database.compileStatement(sql),
            database = database,
            sql = sql,
        )
    }

    override fun insert(table: String, conflictAlgorithm: Int, values: ContentValues): Long {
        return database.insertWithForeignKeyCheck(table, values) {
            database.insert(table, conflictAlgorithm, values)
        }
    }

    override fun delete(table: String, whereClause: String?, whereArgs: Array<out Any?>?): Int {
        return database.delete(table, whereClause, whereArgs)
    }

    override fun update(
        table: String,
        conflictAlgorithm: Int,
        values: ContentValues,
        whereClause: String?,
        whereArgs: Array<out Any?>?
    ): Int {
        return database.update(
            table,
            conflictAlgorithm,
            values,
            whereClause,
            whereArgs,
        )
    }

    override fun execSQL(sql: String) {
        database.withForeignKeyCheck(
            sql = sql,
            query = database::execSQL
        )
    }

    override fun execSQL(sql: String, bindArgs: Array<out Any?>) {
        database.withForeignKeyCheck(
            sql = sql,
            args = bindArgs,
            query = database::execSQL
        )
    }
}
