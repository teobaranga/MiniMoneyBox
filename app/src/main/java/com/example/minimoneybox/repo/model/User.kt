package com.example.minimoneybox.repo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val userId: String,
    @ColumnInfo(name = "BearerToken") val bearerToken: String
)
