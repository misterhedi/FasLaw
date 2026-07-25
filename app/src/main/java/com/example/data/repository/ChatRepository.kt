package com.example.data.repository

import com.example.BuildConfig
import com.example.data.local.dao.ChatDao
import com.example.data.local.entity.ChatMessageEntity
import com.example.domain.model.ChatMessage
import com.example.domain.model.ChatSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ChatRepository(private val chatDao: ChatDao) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getMessages(sessionId: String): Flow<List<ChatMessage>> {
        return chatDao.getMessagesForSession(sessionId).map { entities ->
            entities.map { entity ->
                ChatMessage(
                    id = entity.id,
                    sessionId = entity.sessionId,
                    sender = when (entity.sender) {
                        "USER" -> ChatSender.USER
                        "EXPERT" -> ChatSender.EXPERT
                        else -> ChatSender.AI
                    },
                    text = entity.text,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    suspend fun sendMessageToAi(sessionId: String, userMessageText: String) = withContext(Dispatchers.IO) {
        // Save user message
        chatDao.insertMessage(
            ChatMessageEntity(
                sessionId = sessionId,
                sender = "USER",
                text = userMessageText
            )
        )

        // Generate AI Response
        val aiResponse = fetchGeminiAiResponse(userMessageText)

        // Save AI message
        chatDao.insertMessage(
            ChatMessageEntity(
                sessionId = sessionId,
                sender = "AI",
                text = aiResponse
            )
        )
    }

    suspend fun clearChatSession(sessionId: String) {
        chatDao.clearSession(sessionId)
    }

    private fun fetchGeminiAiResponse(userQuery: String): String {
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
                
                val sysInstruction = "Anda adalah FasLaw AI, asisten konsultan hukum cerdas dari PT Fas Technology Solutions. Anda sangat ahli dalam Hukum Indonesia (KUHP, KUHPerdata, UU Ketenagakerjaan, Hak Konsumen, Hukum Keluarga, Perizinan, Hukum Siber). Berikan jawaban yang terstruktur, bahasa santun & profesional, mengacu pada pasal/undang-undang Indonesia yang relevan, serta sertakan poin rekomendasi praktis."

                val jsonBody = JSONObject().apply {
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().put("text", sysInstruction)))
                    })
                    put("contents", JSONArray().put(
                        JSONObject().put("parts", JSONArray().put(JSONObject().put("text", userQuery)))
                    ))
                }

                val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val responseStr = response.body?.string()

                if (response.isSuccessful && !responseStr.isNullOrBlank()) {
                    val root = JSONObject(responseStr)
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text")
                            if (text.isNotBlank()) {
                                return text + "\n\n---\n*Pemberitahuan: Informasi dari FasLaw AI bersifat edukasi hukum awal dan tidak menggantikan konsultasi resmi dengan advokat berlisensi.*"
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Network or API key error fallback
            }
        }

        // Offline / Smart Fallback Engine based on Indonesian law knowledge base
        return getOfflineSmartLegalAnswer(userQuery)
    }

    private fun getOfflineSmartLegalAnswer(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("penipuan") || q.contains("lapor") -> {
                """
                **Analisis Hukum: Tindak Pidana Penipuan Online**
                
                1. **Landasan Hukum:**
                   • Pasal 378 KUHP tentang Penipuan (Ancaman pidana penjara maks. 4 tahun).
                   • Pasal 28 ayat (1) UU ITE No. 19/2016 jo. Pasal 45A ayat (1) mengenai penipuan melalui media elektronik (Ancaman pidana s/d 6 tahun & denda Rp 1 Miliar).
                
                2. **Langkah Hukum & Prosedur Pelaporan:**
                   • Kumpulkan bukti transfer, tangkapan layar (screenshot) percakapan, dan rincian kontak pelaku.
                   • Laporkan rekening bank pelaku melalui portal resmi **CekRekening.id** (Kemenkominfo) untuk pembekuan rekening.
                   • Buat Laporan Polisi (LP) di Kantor Polres/Polda terdekat pada unit SPKT (Sentra Pelayanan Kepolisian Terpadu).
                   • Pengajuan pemblokiran rekening ke Bank terkait dengan menyertakan Surat Tanda Penerimaan Laporan (STPL) dari Kepolisian.
                
                3. **Rekomendasi Tindakan:**
                   Segera ajukan pengaduan resmi dan konsultasikan dengan advokat mitra FasLaw untuk pendampingan pembuatan draft Laporan Polisi.
                """.trimIndent()
            }
            q.contains("kontrak") || q.contains("karyawan") || q.contains("phk") || q.contains("pesangon") -> {
                """
                **Analisis Hukum Ketenagakerjaan (PKWT & Pesangon)**
                
                1. **Landasan Hukum:**
                   • UU No. 13 Tahun 2003 jo. UU No. 6 Tahun 2023 tentang Cipta Kerja.
                   • Peraturan Pemerintah (PP) No. 35 Tahun 2021 tentang PKWT, Alih Daya, Waktu Kerja, dan Pemutusan Hubungan Kerja (PHK).
                
                2. **Hak Pekerja Kontrak / Tetap:**
                   • Pekerja PKWT berhak atas Uang Kompensasi setelah berakhirnya jangka waktu kerja (Pasal 15 PP 35/2021).
                   • Pekerja Tetap (PKWTT) yang mengalami PHK berhak atas Uang Pesangon, Uang Penghargaan Masa Kerja (UPMK), dan Uang Penggantian Hak (UPH).
                
                3. **Langkah Penyelesaian Perselisihan:**
                   • Perundingan Bipartit (Musyawarah antara Pekerja dan Pengusaha).
                   • Perundingan Tripartit di Dinas Tenaga Kerja (Disnaker) setempat melalui Mediasi.
                   • Gugatan ke Pengadilan Hubungan Industrial (PHI) jika musyawarah tidak mencapai mufakat.
                """.trimIndent()
            }
            q.contains("cerai") || q.contains("perceraian") || q.contains("hak asuh") -> {
                """
                **Analisis Prosedur Hukum Perceraian & Hak Asuh Anak**
                
                1. **Yurisdiksi Pengadilan:**
                   • Bagi Beragama Islam: Gugatan/Permohonan diajukan ke **Pengadilan Agama** di wilayah domisili Istri (Pasal 73 UU No. 7/1989).
                   • Bagi Beragama Non-Islam: Gugatan diajukan ke **Pengadilan Negeri** wilayah domisili Tergugat.
                
                2. **Ketentuan Hak Asuh Anak (Hadhanah):**
                   • Berdasarkan Kompilasi Hukum Islam (KHI) Pasal 105, anak yang belum mumayyiz (di bawah 12 tahun) hak asuhnya diutamakan pada Ibu, kecuali terbukti ada kelalaian berat/ketidakmampuan moral.
                   • Biaya pemeliharaan anak tetap menjadi kewajiban Ayah hingga anak dewasa/mandiri.
                
                3. **Tahapan Persidangan:**
                   1. Pendaftaran Perkara & Pembayaran Panjar Biaya.
                   2. Mediasi Wajib oleh Hakim Mediator.
                   3. Pembacaan Gugatan, Jawaban, Replik, Duplik, Pembuktian, dan Putusan.
                """.trimIndent()
            }
            else -> {
                """
                **Tanggapan Hukum FasLaw AI**
                
                Terima kasih telah berkonsultasi mengenai **"$query"**.
                
                1. **Prinsip Hukum Umum:**
                   Setiap permasalahan hukum perdata maupun pidana di Indonesia diatur dalam regulasi perundang-undangan nasional yang menjamin kepastian hukum serta perlindungan hak bagi setiap warga negara.
                
                2. **Saran Penanganan:**
                   • Pastikan pengumpulan seluruh dokumen bukti tertulis (surat perjanjian, kuitansi, sertifikat, atau korespondensi elektronik).
                   • Konsultasikan perkara Anda secara lebih spesifik melalui fitur **"Chat Advokat"** di aplikasi FasLaw untuk mendapatkan penanganan kasus secara mendalam oleh advokat berlisensi PERADI.
                   • Manfaatkan layanan LBH terdekat jika Anda memerlukan bantuan hukum secara cuma-cuma (pro bono).
                """.trimIndent()
            }
        } + "\n\n---\n*Pemberitahuan: Jawaban ini disajikan secara otomatis oleh sistem kecerdasan buatan FasLaw PT Fas Technology Solutions sebagai informasi awal.*"
    }
}
