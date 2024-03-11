package ru.ktsstudio.fkext.sample.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.ktsstudio.fkext.sample.di.databaseModule
import ru.ktsstudio.fkext.sample.di.mainModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                databaseModule,
                mainModule,
            )
        }
    }
}