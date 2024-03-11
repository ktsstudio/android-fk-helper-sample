package ru.ktsstudio.fkext.sample.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.ktsstudio.fkext.sample.data.database.FinanceDB

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            FinanceDB::class.java,
            FinanceDB.DATABASE_NAME,
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