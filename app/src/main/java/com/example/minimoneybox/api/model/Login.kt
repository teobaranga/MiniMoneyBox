package com.example.minimoneybox.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(

    @Json(name = "User")
    val user: User,

    @Json(name = "Session")
    val session: Session
) {
    @JsonClass(generateAdapter = true)
    data class User(val UserId: String)

    @JsonClass(generateAdapter = true)
    data class Session(val BearerToken: String)
}

@JsonClass(generateAdapter = true)
data class LoginRequest(

    @Json(name = "Email")
    val email: String,

    @Json(name = "Password")
    val password: String,

    @Json(name = "Idfa")
    val idfa: String = "ANYTHING"
)
