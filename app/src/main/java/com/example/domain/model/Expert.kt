package com.example.domain.model

data class Expert(
    val id: String,
    val name: String,
    val title: String, // e.g. "Advokat Senior - DPC PERADI Jakarta"
    val specialization: String, // e.g. "Hukum Pidana & Perdata"
    val rating: Float, // e.g. 4.9
    val totalConsultations: Int, // e.g. 142
    val isOnline: Boolean,
    val avatarUrl: String? = null
)
