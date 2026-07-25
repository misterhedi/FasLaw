package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.DocumentAnalysisEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM document_analysis_history ORDER BY timestamp DESC")
    fun getAllDocumentHistory(): Flow<List<DocumentAnalysisEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocumentHistory(document: DocumentAnalysisEntity): Long

    @Query("DELETE FROM document_analysis_history WHERE id = :id")
    suspend fun deleteById(id: Long)
}
