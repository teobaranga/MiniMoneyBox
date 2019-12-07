package com.example.minimoneybox.api

import com.example.minimoneybox.api.model.LoginRequest
import com.example.minimoneybox.repo.LoginInfoDao
import com.example.minimoneybox.repo.UserDao
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Retrofit [Authenticator] that refreshes the Bearer token used to make calls to the MoneyBox API when it is
 * expired.
 */
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
                    .blockingGet()

                val bearerToken = loginResponse.session.BearerToken

                Timber.d("Got new Bearer token: $bearerToken")

                userDao.updateBearer(bearerToken)

                // Update the header of the rejected request and retry it
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $bearerToken")
                    .build()

            } catch (e: Exception) {
                Timber.e(e, "Error encountered while refreshing the Bearer token")
                return null
            }
        }

        Timber.w("No login info, cannot refresh Bearer token")

        return null
    }
}
