package com.example.minimoneybox.di

import com.example.minimoneybox.api.MoneyBoxApi
import com.example.minimoneybox.repo.LoginInfoDao
import com.example.minimoneybox.repo.ProductsDao
import com.example.minimoneybox.repo.UserDao
import io.mockk.mockk
import org.koin.dsl.module

/**
 * Koin module common to all unit tests.
 */
val testModule = module {
    single { mockk<MoneyBoxApi>(relaxed = true) }
    single { mockk<LoginInfoDao>(relaxed = true) }
    single { mockk<UserDao>(relaxed = true) }
    single { mockk<ProductsDao>(relaxed = true) }
}
