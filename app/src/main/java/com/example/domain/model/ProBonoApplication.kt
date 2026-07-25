package com.example.domain.model

data class ProBonoApplication(
    val ticketId: String,
    val category: String,
    val summary: String,
    val urgency: String,
    val applicantName: String,
    val phone: String,
    val email: String = "",
    val city: String,
    val financialDeclaration: String,
    val preferredContact: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Menunggu Verifikasi",
    val assignedLbhName: String = "Posbakum LBH Terdekat"
)
