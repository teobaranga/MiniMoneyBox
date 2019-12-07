package com.example.minimoneybox.ui.account

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.RxJavaViewModel
import com.example.minimoneybox.api.ApiFactory
import com.example.minimoneybox.api.model.PaymentRequest
import com.example.minimoneybox.repo.AppDatabase
import com.example.minimoneybox.repo.model.Product
import com.example.minimoneybox.repo.model.ProductsData
import io.reactivex.schedulers.Schedulers

class AccountViewModel(application: Application) : RxJavaViewModel(application) {

    private val moneyBoxApi = ApiFactory.getMoneyBoxApi(application.applicationContext)

    private val productsDao = AppDatabase.get(application.applicationContext).productsDao()

    val product = MediatorLiveData<Product>()

    val addMoneyValue = MutableLiveData<Double>(10.0) as LiveData<Double>

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

    fun addMoney() {
        lastProduct?.value?.let { product ->
            val amount = addMoneyValue.value!!
            val paymentRequest = PaymentRequest(amount, product.id)
            moneyBoxApi.addMoney(paymentRequest)
                .andThen(moneyBoxApi.getProducts()
                    .doOnSuccess {
                        println("Products retrieved: $it")
                    }
                    .flatMapCompletable {
                        productsDao.insertProductsData(ProductsData(totalPlanValue = it.totalPlanValue))
                            .andThen(productsDao.insertProducts(it.products.map {
                                Product(it.id, it.planValue, it.moneyBox, it.details.name)
                            }))
                            .doOnComplete {
                                println("Products saved")
                            }
                    })
                .subscribeOn(Schedulers.io())
                .subscribe({
                    // Success, nothing to do
                }, {
                    it.printStackTrace()
                })
        }
    }
}
