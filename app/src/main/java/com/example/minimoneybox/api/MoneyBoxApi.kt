package com.example.minimoneybox.api

import com.example.minimoneybox.api.model.LoginRequest
import com.example.minimoneybox.api.model.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface MoneyBoxApi {

    @POST("users/login")
    fun login(@Body loginRequest: LoginRequest): Observable<LoginResponse>
}
