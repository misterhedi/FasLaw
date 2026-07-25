package com.example.domain.model

data class DocumentAnalysisResult(
    val id: Long = 0,
    val fileName: String,
    val summary: String,
    val keyPoints: List<String>,
    val relatedArticles: List<String>,
    val recommendations: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)
