package com.example.minimoneybox.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentRequest(
    @Json(name = "Amount")
    val amount: Double,

    @Json(name = "InvestorProductId")
    val productId: Long
)
