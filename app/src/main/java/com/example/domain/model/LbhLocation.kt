package com.example.domain.model

data class LbhLocation(
    val id: String,
    val name: String,
    val distanceKm: Double,
    val address: String,
    val phone: String,
    val operatingHours: String,
    val latitude: Double,
    val longitude: Double,
    val isVerifiedPosbakum: Boolean = true
)
