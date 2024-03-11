package ru.ktsstudio.sample.utils.sqlite_framework

import androidx.sqlite.db.SupportSQLiteOpenHelper

internal class FinanceQLiteOpenHelperFactory(
    private val delegate: SupportSQLiteOpenHelper.Factory
) : SupportSQLiteOpenHelper.Factory {

    override fun create(
        configuration: SupportSQLiteOpenHelper.Configuration
    ): SupportSQLiteOpenHelper {
        return InterceptorSQLiteOpenHelper(openHelper = delegate.create(configuration))
    }
}
