package com.example.minimoneybox.ui.main

import android.app.Application
import androidx.lifecycle.map
import com.example.minimoneybox.RxJavaViewModel
import com.example.minimoneybox.repo.AppDatabase
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : RxJavaViewModel(application) {

    private val userDao = AppDatabase.get(application.applicationContext).userDao()

    private val productsDao = AppDatabase.get(application.applicationContext).productsDao()

    private val loginInfoDao = AppDatabase.get(application.applicationContext).loginInfoDao()

    val currentUser = userDao.getCurrentUser()

    val userHasName = currentUser.map { it?.name?.isNotBlank() }

    val products = productsDao.getProducts()

    fun signOut() {
        val disposable = productsDao.clear()
            .andThen(userDao.clear())
            .andThen(loginInfoDao.clear())
            .subscribeOn(Schedulers.io())
            .subscribe({
                println("Sign out success")
            }, {
                System.err.println("Sign out error")
            })
        addDisposable(disposable)
    }
}
