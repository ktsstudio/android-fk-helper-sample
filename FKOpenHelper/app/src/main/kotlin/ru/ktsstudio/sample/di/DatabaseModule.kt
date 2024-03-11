package ru.ktsstudio.sample.di

import androidx.room.Room
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.ktsstudio.sample.data.database.FinanceDB
import ru.ktsstudio.sample.utils.sqlite_framework.FinanceQLiteOpenHelperFactory

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            FinanceDB::class.java,
            FinanceDB.DATABASE_NAME,
        )
            .openHelperFactory(
                factory = FinanceQLiteOpenHelperFactory(
                    delegate = FrameworkSQLiteOpenHelperFactory()
                )
            )
            .build()
    }

    factory {
        get<FinanceDB>().billDao()
    }
    factory {
        get<FinanceDB>().userDao()
    }
    factory {
        get<FinanceDB>().transactionDao()
    }
    factory {
        get<FinanceDB>().categoryDao()
    }
}