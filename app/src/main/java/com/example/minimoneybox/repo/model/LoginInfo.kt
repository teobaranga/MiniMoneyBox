package com.example.minimoneybox.repo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Login information. This information is cached so that it can be used
 * when refreshing the Bearer token used for the MoneyBox API.
 */
@Entity
data class LoginInfo(
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @PrimaryKey val id: String = "loginInfo"
)
