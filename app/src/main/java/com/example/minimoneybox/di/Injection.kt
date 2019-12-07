package com.example.minimoneybox.di

import com.example.minimoneybox.api.ApiFactory
import com.example.minimoneybox.repo.AppDatabase
import com.example.minimoneybox.ui.account.AccountViewModel
import com.example.minimoneybox.ui.login.LoginViewModel
import com.example.minimoneybox.ui.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel { LoginViewModel(moneyBoxApi = get(), loginInfoDao = get(), userDao = get(), productsDao = get()) }
}

val mainModule = module {
    viewModel { MainViewModel(loginInfoDao = get(), userDao = get(), productsDao = get()) }
}

val accountModule = module {
    viewModel { AccountViewModel(moneyBoxApi = get(), productsDao = get()) }
}

val appModule = module {

    // MoneyBox API
    single { ApiFactory.getMoneyBoxApi(appContext = get()) }

    // Room database
    single { AppDatabase.get(applicationContext = get()) }

    // LoginInfo DAO
    single {
        val database: AppDatabase = get()
        return@single database.loginInfoDao()
    }
    // User DAO
    single {
        val database: AppDatabase = get()
        return@single database.userDao()
    }
    // Products DAO
    single {
        val database: AppDatabase = get()
        return@single database.productsDao()
    }

}
