package com.example.domain.model

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val avatarUrl: String? = null
)
