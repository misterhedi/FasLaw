package com.example.domain.model

data class LawPasal(
    val id: String,
    val lawName: String, // e.g. "KUHP (Kitab Undang-Undang Hukum Pidana)"
    val pasalNumber: String, // e.g. "Pasal 362"
    val title: String, // e.g. "Pencurian"
    val content: String,
    val penaltyDescription: String
)
