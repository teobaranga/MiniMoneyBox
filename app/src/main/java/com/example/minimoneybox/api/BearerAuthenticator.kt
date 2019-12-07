package com.example.minimoneybox.api

import com.example.minimoneybox.api.model.LoginRequest
import com.example.minimoneybox.repo.LoginInfoDao
import com.example.minimoneybox.repo.UserDao
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.TimeUnit

class BearerAuthenticator(
    private val moneyBoxApi: Lazy<MoneyBoxApi>,
    private val loginInfoDao: LoginInfoDao,
    private val userDao: UserDao
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        // Refresh the bearer token using a synchronous api request

        val loginInfo = loginInfoDao.getSync()

        if (loginInfo != null) {

            try {
                val loginResponse = moneyBoxApi.value.login(LoginRequest(loginInfo.email, loginInfo.password))
                    .timeout(15_000, TimeUnit.MILLISECONDS)
                    .blockingFirst()

                val bearerToken = loginResponse.session.BearerToken

                println("Got new Bearer token: $bearerToken")

                userDao.updateBearer(bearerToken)

                // Add new header to rejected request and retry it
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $bearerToken")
                    .build()

            } catch (e: Exception) {
                System.err.println(e)
                return null
            }
        }

        System.err.println("No login info")

        return null
    }
}
