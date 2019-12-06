package com.example.minimoneybox.api

import android.content.Context
import com.example.minimoneybox.BuildConfig
import com.example.minimoneybox.repo.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {

    private lateinit var moneyBoxApi: MoneyBoxApi

    fun getMoneyBoxApi(appContext: Context): MoneyBoxApi {
        moneyBoxApi = if (::moneyBoxApi.isInitialized) {
            moneyBoxApi
        } else {
            val appDatabase = AppDatabase.get(appContext)

            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(AuthInterceptor(appDatabase.userDao()))
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                })
                .authenticator(BearerAuthenticator(lazy { moneyBoxApi }, appDatabase.loginInfoDao(), appDatabase.userDao()))
                .build()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api-test01.moneyboxapp.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()


            retrofit.create(MoneyBoxApi::class.java)
        }
        return moneyBoxApi
    }
}
