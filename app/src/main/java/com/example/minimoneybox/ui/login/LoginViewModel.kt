package com.example.minimoneybox.ui.login

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.example.minimoneybox.RxJavaViewModel
import com.example.minimoneybox.api.ApiFactory
import com.example.minimoneybox.api.model.LoginRequest
import com.example.minimoneybox.repo.AppDatabase
import com.example.minimoneybox.repo.model.LoginInfo
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : RxJavaViewModel(application) {

    private val moneyBoxApi = ApiFactory.moneyBoxApi

    private val loginInfoDao = AppDatabase.get(application.applicationContext).loginInfoDao()

    val email = MediatorLiveData<String>()

    val password = MediatorLiveData<String>()

    init {
        // Restore any previously cached login information
        val loginInfo = loginInfoDao.get()
        email.addSource(loginInfo) {
            email.value = it.email
            email.removeSource(loginInfo)
        }
        password.addSource(loginInfo) {
            password.value = it.password
            password.removeSource(loginInfo)
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
                .subscribe({
                    println(it)
                }, {
                    System.err.println(it)
                })

            addDisposable(disposable)
        }
    }
}
