package ru.ktsstudio.sample.utils.sqlite_framework

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteStatement
import ru.ktsstudio.sample.utils.sqlite_framework.utils.withForeignKeyCheck

internal class InterceptorSQLiteStatement(
    private val supportSQLiteStatement: SupportSQLiteStatement,
    private val database: SupportSQLiteDatabase,
    private val sql: String
) : SupportSQLiteStatement by supportSQLiteStatement {

    private val bindArgsCache: MutableList<Any?> = mutableListOf()

    override fun bindNull(index: Int) {
        saveArgsToCache(index, null)
        supportSQLiteStatement.bindNull(index)
    }

    override fun bindLong(index: Int, value: Long) {
        saveArgsToCache(index, value)
        supportSQLiteStatement.bindLong(index, value)
    }

    override fun bindDouble(index: Int, value: Double) {
        saveArgsToCache(index, value)
        supportSQLiteStatement.bindDouble(index, value)
    }

    override fun bindString(index: Int, value: String) {
        saveArgsToCache(index, value)
        supportSQLiteStatement.bindString(index, value)
    }

    override fun bindBlob(index: Int, value: ByteArray) {
        saveArgsToCache(index, value)
        supportSQLiteStatement.bindBlob(index, value)
    }

    override fun clearBindings() {
        bindArgsCache.clear()
        supportSQLiteStatement.clearBindings()
    }

    override fun execute() {
        database.withForeignKeyCheck(sql, bindArgsCache.toTypedArray()) { _, _ ->
            supportSQLiteStatement.execute()
        }
    }

    override fun executeUpdateDelete(): Int {
        return database.withForeignKeyCheck(sql, bindArgsCache.toTypedArray()) { _, _ ->
            supportSQLiteStatement.executeUpdateDelete()
        }
    }

    override fun executeInsert(): Long {
        return database.withForeignKeyCheck(sql, bindArgsCache.toTypedArray()) { _, _ ->
            supportSQLiteStatement.executeInsert()
        }
    }

    private fun saveArgsToCache(bindIndex: Int, value: Any?) {
        val index = bindIndex - 1
        if (index >= bindArgsCache.size) {
            // Add null entries to the list until we have the desired # of indices
            for (i in bindArgsCache.size..index) {
                bindArgsCache.add(null)
            }
        }
        bindArgsCache[index] = value
    }
}
