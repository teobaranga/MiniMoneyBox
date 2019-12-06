package com.example.minimoneybox.repo.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity
data class Product(
    @PrimaryKey val id: Long,
    val planValue: Double,
    val moneyBox: Double,
    val name: String,
    val key: String = "products"
)

@Entity
data class ProductsData(
    @PrimaryKey val key: String = "products",
    val totalPlanValue: Double
)

data class InvestorProducts(
    @Embedded
    val productsData: ProductsData,

    @Relation(parentColumn = "key", entityColumn = "key")
    val products: List<Product>
)
