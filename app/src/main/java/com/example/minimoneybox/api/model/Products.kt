package com.example.minimoneybox.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDetails(
    @Json(name = "FriendlyName")
    val name: String
)

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "Id")
    val id: Long,

    @Json(name = "PlanValue")
    val planValue: Double,

    @Json(name = "Moneybox")
    val moneyBox: Double,

    @Json(name = "Product")
    val details: ProductDetails
)

@JsonClass(generateAdapter = true)
data class InvestorProducts(
    @Json(name = "TotalPlanValue")
    val totalPlanValue: Double,

    @Json(name = "ProductResponses")
    val products: List<Product>
)
