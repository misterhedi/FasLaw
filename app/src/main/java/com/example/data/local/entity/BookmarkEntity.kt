package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val articleId: String,
    val title: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)
