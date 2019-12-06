package com.example.minimoneybox.api.model

import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    @PrimaryKey val Id: Long
)

@JsonClass(generateAdapter = true)
data class InvestorProducts(
    @Json(name = "TotalPlanValue")
    val totalPlanValue: Double,

    @Json(name = "ProductResponses")
    val products: List<Product>
)
