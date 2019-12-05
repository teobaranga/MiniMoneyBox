package com.example.minimoneybox.api

import com.example.minimoneybox.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {

    /**
     * Interceptor that adds all the required headers for the MoneyBox REST API.
     */
    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .header("AppId", BuildConfig.APP_ID)
            .header("Content-Type", "application/json")
            .header("appVersion", "5.10.0")
            .header("apiVersion", "3.0.0")
            .build()

        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api-test01.moneyboxapp.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .build()


    val moneyBoxApi: MoneyBoxApi = retrofit.create(MoneyBoxApi::class.java)
}
