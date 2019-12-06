package com.example.minimoneybox.ui.account

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.RxJavaViewModel
import com.example.minimoneybox.repo.AppDatabase
import com.example.minimoneybox.repo.model.Product

class AccountViewModel(application: Application) : RxJavaViewModel(application) {

    private val productsDao = AppDatabase.get(application.applicationContext).productsDao()

    val product = MediatorLiveData<Product>()

    val addMoneyValue = MutableLiveData<Float>(10f) as LiveData<Float>

    private var lastProduct: LiveData<Product>? = null

    fun loadProduct(id: Long) {
        lastProduct?.let {
            product.removeSource(it)
        }
        lastProduct = productsDao.getProduct(id).apply {
            product.addSource(this) {
                product.value = it
            }
        }
    }
}
