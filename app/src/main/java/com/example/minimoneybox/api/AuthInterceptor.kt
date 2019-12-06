package com.example.minimoneybox.api

import com.example.minimoneybox.BuildConfig
import com.example.minimoneybox.repo.UserDao
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor that adds all the required headers for the MoneyBox REST API.
 */
class AuthInterceptor(private val userDao: UserDao): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        var requestBuilder = chain.request()
            .newBuilder()
            .header("AppId", BuildConfig.APP_ID)
            .header("Content-Type", "application/json")
            .header("appVersion", "5.10.0")
            .header("apiVersion", "3.0.0")

        val user = userDao.getCurrentUserSync()
        if (user != null) {
            requestBuilder = requestBuilder.header("Authorization", "Bearer ${user.bearerToken}")
        }

        return chain.proceed(requestBuilder.build())
    }
}
