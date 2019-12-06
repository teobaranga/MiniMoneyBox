package com.example.minimoneybox.ui.login

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.minimoneybox.RxJavaViewModel
import com.example.minimoneybox.api.ApiFactory
import com.example.minimoneybox.api.model.LoginRequest
import com.example.minimoneybox.repo.AppDatabase
import com.example.minimoneybox.repo.model.LoginInfo
import com.example.minimoneybox.repo.model.Product
import com.example.minimoneybox.repo.model.ProductsData
import com.example.minimoneybox.repo.model.User
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : RxJavaViewModel(application) {

    private val moneyBoxApi = ApiFactory.getMoneyBoxApi(application.applicationContext)

    private val loginInfoDao = AppDatabase.get(application.applicationContext).loginInfoDao()

    private val userDao = AppDatabase.get(application.applicationContext).userDao()

    private val productsDao = AppDatabase.get(application.applicationContext).productsDao()

    val email = MediatorLiveData<String>()

    val password = MediatorLiveData<String>()

    val fullName = MutableLiveData<String>()

    val signedIn = userDao.getCurrentUser().map { it?.signedIn }

    init {
        // Restore any previously cached login information
        val loginInfo = loginInfoDao.get()
        email.addSource(loginInfo) {
            if (it != null) {
                email.value = it.email
                email.removeSource(loginInfo)
            }
        }
        password.addSource(loginInfo) {
            if (it != null) {
                password.value = it.password
                password.removeSource(loginInfo)
            }
        }
    }

    fun login() {

        val email = email.value
        val password = password.value

        if (email != null && password != null) {

            val cacheLoginInfoDisposable = loginInfoDao.insert(LoginInfo(email, password))
                .subscribeOn(Schedulers.io())
                .subscribe({
                    // Success, nothing to do
                }, {
                    System.err.println("Error while caching login data: $it")
                })
            addDisposable(cacheLoginInfoDisposable)

            val loginRequest = LoginRequest(email, password)

            val disposable = moneyBoxApi.login(loginRequest)
                .flatMapCompletable {

                    val fullName = with(fullName.value?.trim()) {
                        if (this?.isNotBlank() == true) {
                            this
                        } else {
                            null
                        }
                    }

                    val user = User(it.user.UserId, it.session.BearerToken, fullName)

                    println("Saving user: $user")

                    userDao.insert(user)
                }
                .andThen(moneyBoxApi.getProducts()
                    .doOnSuccess {
                        println("Products retrieved: $it")
                    }
                    .flatMapCompletable {
                        productsDao.insertProductsData(ProductsData(totalPlanValue = it.totalPlanValue))
                            .andThen(productsDao.insertProducts(it.products.map { Product(it.Id) }))
                            .doOnComplete {
                                println("Products saved")
                            }
                    })
                .andThen(userDao.signIn())
                .subscribe({
                    println("User login success")
                }, {
                    System.err.println(it)
                })

            addDisposable(disposable)
        }
    }
}
