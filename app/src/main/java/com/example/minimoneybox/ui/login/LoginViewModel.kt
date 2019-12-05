package com.example.minimoneybox.ui.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.RxJavaViewModel
import com.example.minimoneybox.api.ApiFactory
import com.example.minimoneybox.api.model.LoginRequest
import com.example.minimoneybox.repo.AppDatabase
import com.example.minimoneybox.repo.model.LoginInfo
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : RxJavaViewModel(application) {

    private val moneyBoxApi = ApiFactory.moneyBoxApi

    private val loginInfoDao = AppDatabase.get(application.applicationContext).loginInfoDao()

    val email = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    val loginInfo = loginInfoDao.get()

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
