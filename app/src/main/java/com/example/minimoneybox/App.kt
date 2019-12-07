package com.example.minimoneybox

import android.app.Application
import com.example.minimoneybox.di.accountModule
import com.example.minimoneybox.di.appModule
import com.example.minimoneybox.di.loginModule
import com.example.minimoneybox.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, loginModule, mainModule, accountModule))
        }
    }
}
