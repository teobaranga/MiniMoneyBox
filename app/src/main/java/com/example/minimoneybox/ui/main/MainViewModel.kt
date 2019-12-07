package com.example.minimoneybox.ui.main

import androidx.lifecycle.map
import com.example.minimoneybox.RxJavaViewModel
import com.example.minimoneybox.repo.LoginInfoDao
import com.example.minimoneybox.repo.ProductsDao
import com.example.minimoneybox.repo.UserDao
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val loginInfoDao: LoginInfoDao,
    private val userDao: UserDao,
    private val productsDao: ProductsDao
) : RxJavaViewModel() {

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
