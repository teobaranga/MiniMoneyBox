package com.example.minimoneybox.api

import com.example.minimoneybox.api.model.LoginRequest
import com.example.minimoneybox.api.model.LoginResponse
import com.example.minimoneybox.api.model.InvestorProducts
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MoneyBoxApi {

    @POST("users/login")
    fun login(@Body loginRequest: LoginRequest): Observable<LoginResponse>

    @GET("investorproducts")
    fun getProducts(): Single<InvestorProducts>
}
