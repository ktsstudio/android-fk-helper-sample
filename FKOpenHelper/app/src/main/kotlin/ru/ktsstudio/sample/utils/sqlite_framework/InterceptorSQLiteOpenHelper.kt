package ru.ktsstudio.sample.utils.sqlite_framework

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

internal class InterceptorSQLiteOpenHelper(
    private val openHelper: SupportSQLiteOpenHelper
) : SupportSQLiteOpenHelper by openHelper {

    override val readableDatabase: SupportSQLiteDatabase
        get() = InterceptorSQLiteDatabase(openHelper.readableDatabase)

    override val writableDatabase: SupportSQLiteDatabase
        get() = InterceptorSQLiteDatabase(openHelper.writableDatabase)
}
