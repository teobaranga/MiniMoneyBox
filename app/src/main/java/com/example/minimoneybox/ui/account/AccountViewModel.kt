package com.example.minimoneybox.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.RxJavaViewModel
import com.example.minimoneybox.api.MoneyBoxApi
import com.example.minimoneybox.api.model.PaymentRequest
import com.example.minimoneybox.repo.ProductsDao
import com.example.minimoneybox.repo.model.Product
import com.example.minimoneybox.repo.model.ProductsData
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class AccountViewModel(
    private val moneyBoxApi: MoneyBoxApi,
    private val productsDao: ProductsDao
) : RxJavaViewModel() {

    /**
     * The [Product] to be displayed by the view. The source of this value is the latest [lastProduct].
     */
    val product = MediatorLiveData<Product>()

    /**
     * The value to be added when clicking the "Add Money" button. Currently hardcoded to 10 pounds.
     */
    val addMoneyValue = MutableLiveData<Double>(10.0) as LiveData<Double>

    /**
     * The last product requested to be displayed. Since views may request to load multiple products
     * during the life of the ViewModel, it must be ensured that only the last requested product is displayed.
     */
    private var lastProduct: LiveData<Product>? = null

    /**
     * Load the product with the given ID.
     */
    fun loadProduct(id: Long) {
        // Remove any previous product source from the main product LiveData
        lastProduct?.let {
            product.removeSource(it)
        }
        // Add the new product source
        lastProduct = productsDao.getProduct(id).apply {
            product.addSource(this) {
                product.value = it
            }
        }
    }

    /**
     * Add a constant amount of money to the currently loaded product.
     */
    fun addMoney() {
        lastProduct?.value?.let { product ->
            val amount = addMoneyValue.value!!
            val paymentRequest = PaymentRequest(amount, product.id)
            val disposable = moneyBoxApi.addMoney(paymentRequest)
                // Refresh all the products on success
                .andThen(moneyBoxApi.getProducts()
                    .doOnSuccess {
                        Timber.d("Products retrieved: $it")
                    }
                    .flatMapCompletable {
                        productsDao.insertProductsData(ProductsData(totalPlanValue = it.totalPlanValue))
                            .andThen(productsDao.insertProducts(it.products.map {
                                Product(it.id, it.planValue, it.moneyBox, it.details.name)
                            }))
                            .doOnComplete {
                                Timber.d("Products saved")
                            }
                    })
                .subscribeOn(Schedulers.io())
                .subscribe({
                    // Success, nothing to do
                }, {
                    Timber.w(it, "Error while adding money")
                })
            addDisposable(disposable)
        }
    }
}
