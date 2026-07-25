package com.example.data.repository

import com.example.data.local.dao.DocumentDao
import com.example.data.local.entity.DocumentAnalysisEntity
import com.example.domain.model.DocumentAnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray

class DocumentRepository(private val documentDao: DocumentDao) {

    fun getDocumentHistory(): Flow<List<DocumentAnalysisResult>> {
        return documentDao.getAllDocumentHistory().map { entities ->
            entities.map { entity ->
                DocumentAnalysisResult(
                    id = entity.id,
                    fileName = entity.fileName,
                    summary = entity.summary,
                    keyPoints = parseJsonList(entity.keyPointsJson),
                    relatedArticles = parseJsonList(entity.relatedArticlesJson),
                    recommendations = parseJsonList(entity.recommendationsJson),
                    timestamp = entity.timestamp
                )
            }
        }
    }

    suspend fun analyzeDocument(fileName: String): Result<DocumentAnalysisResult> = withContext(Dispatchers.IO) {
        delay(2000) // Simulate document OCR and Gemini API AI document parsing

        val keyPoints = listOf(
            "Identitas Pihak Pertama dan Pihak Kedua tercantum dengan sah.",
            "Klausula Penyelesaian Sengketa mengatur jalur Mediasi dan Tahap Pengadilan Negeri.",
            "Terdapat poin pinalti pengakhiran sepihak sebesar 10% dari total nilai kontrak.",
            "Jangka waktu perjanjian berlaku selama 12 bulan sejak penandatanganan."
        )

        val relatedArticles = listOf(
            "Pasal 1320 KUHPerdata tentang Syarat Sahnya Perjanjian",
            "Pasal 1338 KUHPerdata tentang Asas Pacta Sunt Servanda (Perjanjian Mengikat Sebagai UU)",
            "Pasal 1243 KUHPerdata tentang Ganti Rugi akibat Wanprestasi"
        )

        val recommendations = listOf(
            "Tambahkan klausula Force Majeure (Keadaan Memaksa) yang lebih spesifik mencakup bencana alam & regulasi pemerintah.",
            "Pastikan spesifikasi objek kerja dan tahapan pembayaran (termin) dilampirkan pada Lampiran 1.",
            "Lakukan legal drafting review ulang dengan advokat mitra FasLaw sebelum melakukan penandatanganan di atas meterai Rp10.000."
        )

        val summary = "Dokumen ini merupakan Draft Surat Perjanjian Kerjasama Bisnis/Sewa yang memuat kewajiban umum para pihak, hak imbalan, serta skema pembatalan kontrak. Secara umum klausa memenuhi syarat sah KUHPerdata, namun disarankan penguatan pada klausa keadaan memaksa."

        val result = DocumentAnalysisResult(
            fileName = fileName,
            summary = summary,
            keyPoints = keyPoints,
            relatedArticles = relatedArticles,
            recommendations = recommendations
        )

        // Save to Room DB
        val entity = DocumentAnalysisEntity(
            fileName = fileName,
            summary = summary,
            keyPointsJson = toJsonList(keyPoints),
            relatedArticlesJson = toJsonList(relatedArticles),
            recommendationsJson = toJsonList(recommendations)
        )

        val savedId = documentDao.insertDocumentHistory(entity)

        Result.success(result.copy(id = savedId))
    }

    private fun toJsonList(list: List<String>): String {
        val jsonArray = JSONArray()
        list.forEach { jsonArray.put(it) }
        return jsonArray.toString()
    }

    private fun parseJsonList(jsonStr: String): List<String> {
        return try {
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}
