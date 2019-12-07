package com.example.minimoneybox.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity that holds the current user's information.
 */
@Entity
data class User(
    @PrimaryKey val userId: String,
    val bearerToken: String,
    val name: String? = null,
    val signedIn: Boolean = false
)
