package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "document_analysis_history")
data class DocumentAnalysisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fileName: String,
    val summary: String,
    val keyPointsJson: String, // Stored as JSON string list
    val relatedArticlesJson: String, // Stored as JSON string list
    val recommendationsJson: String, // Stored as JSON string list
    val timestamp: Long = System.currentTimeMillis()
)
