package com.example.domain.model

data class Article(
    val id: String,
    val title: String,
    val category: String, // "Pidana", "Perdata", "Keluarga", "Ketenagakerjaan", "Konsumen"
    val date: String,
    val readTimeMinutes: Int,
    val summary: String,
    val contentHtml: String,
    val isBookmarked: Boolean = false
)
