package com.example.minimoneybox.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minimoneybox.repo.model.InvestorProducts
import com.example.minimoneybox.repo.model.Product
import com.example.minimoneybox.repo.model.ProductsData
import io.reactivex.Completable

@Dao
abstract class ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProductsData(productsData: ProductsData): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProducts(products: List<Product>): Completable

    @Transaction
    @Query("SELECT * FROM ProductsData")
    abstract fun getProducts(): LiveData<InvestorProducts>

    @Query("SELECT * FROM Product WHERE id = :id")
    abstract fun getProduct(id: Long): LiveData<Product>

    @Query("DELETE FROM ProductsData")
    abstract fun clearProductsData(): Completable

    @Query("DELETE FROM Product")
    abstract fun clearProducts(): Completable

    fun clear(): Completable {
        return clearProducts().andThen(clearProductsData())
    }
}
