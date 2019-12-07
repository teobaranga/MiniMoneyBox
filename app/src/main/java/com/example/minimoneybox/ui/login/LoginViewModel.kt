package com.example.minimoneybox.ui.login

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.minimoneybox.R
import com.example.minimoneybox.RxJavaViewModel
import com.example.minimoneybox.api.ApiFactory
import com.example.minimoneybox.api.model.LoginRequest
import com.example.minimoneybox.repo.AppDatabase
import com.example.minimoneybox.repo.model.LoginInfo
import com.example.minimoneybox.repo.model.Product
import com.example.minimoneybox.repo.model.ProductsData
import com.example.minimoneybox.repo.model.User
import io.reactivex.Completable
import retrofit2.HttpException
import timber.log.Timber

class LoginViewModel(application: Application) : RxJavaViewModel(application) {

    private val moneyBoxApi = ApiFactory.getMoneyBoxApi(application.applicationContext)

    private val loginInfoDao = AppDatabase.get(application.applicationContext).loginInfoDao()

    private val userDao = AppDatabase.get(application.applicationContext).userDao()

    private val productsDao = AppDatabase.get(application.applicationContext).productsDao()

    val email = MediatorLiveData<String>()

    val password = MediatorLiveData<String>()

    val fullName = MutableLiveData<String>()

    val errorMessage = MutableLiveData<@StringRes Int>(R.string.empty)

    val signedIn = userDao.getCurrentUser().map { it?.signedIn }

    /**
     * Pull the credentials provided by the user and attempt to login.
     */
    fun login() {

        // Clear any previous error messages
        errorMessage.value = R.string.empty

        val email = email.value
        val password = password.value

        if (email != null && password != null) {

            val loginRequest = LoginRequest(email, password)

            // Login
            val disposable = moneyBoxApi.login(loginRequest)
                // Store the user information locally
                .flatMapCompletable {
                    val fullName = with(fullName.value?.trim()) {
                        if (this?.isNotBlank() == true) {
                            this
                        } else {
                            null
                        }
                    }

                    val user = User(it.user.UserId, it.session.BearerToken, fullName)

                    Timber.d("Saving user: $user")

                    userDao.insert(user)
                }
                // Fetch the user's products
                .andThen(moneyBoxApi.getProducts()
                    .doOnSuccess {
                        Timber.d("Products retrieved: $it")
                    }
                    // Save the products to the local database
                    .flatMapCompletable {
                        productsDao.insertProductsData(ProductsData(totalPlanValue = it.totalPlanValue))
                            .andThen(productsDao.insertProducts(it.products.map {
                                Product(it.id, it.planValue, it.moneyBox, it.details.name)
                            }))
                            .doOnComplete {
                                Timber.d("Products saved")
                            }
                    })
                // Cache the login information for token refresh purposes
                .andThen(loginInfoDao.insert(LoginInfo(email, password))
                    .onErrorResumeNext {
                        // Log but ignore errors about login info
                        Timber.e(it,"Error while caching login info")
                        Completable.complete()
                    })
                // Mark the user as signed in to communicate success to the activity
                .andThen(userDao.signIn())
                .subscribe({
                    Timber.d("User login success")
                }, {
                    val error = when(it) {
                        is HttpException -> {
                            when (it.code()) {
                                401 -> {
                                    R.string.error_401
                                }
                                400 -> {
                                    // Bad request = invalid input
                                    R.string.error_400
                                }
                                else -> R.string.error_unknown
                            }
                        }
                        else -> R.string.error_unknown
                    }

                    errorMessage.postValue(error)
                })

            addDisposable(disposable)
        }
    }
}
