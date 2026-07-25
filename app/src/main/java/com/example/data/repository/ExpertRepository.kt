package com.example.data.repository

import com.example.domain.model.Expert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ExpertRepository {

    private val expertsList = listOf(
        Expert(
            id = "exp_1",
            name = "Adv. Hendra Wijaya, S.H., M.H.",
            title = "Advokat Senior - DPC PERADI Jakarta",
            specialization = "Hukum Pidana, Tipikor & Cyber Law",
            rating = 4.9f,
            totalConsultations = 215,
            isOnline = true
        ),
        Expert(
            id = "exp_2",
            name = "Adv. Ratna Kusuma, S.H.",
            title = "Konsultan Hukum Keluarga & Waris",
            specialization = "Hukum Perceraian, Hak Asuh & Harta Gono-Gini",
            rating = 4.8f,
            totalConsultations = 184,
            isOnline = true
        ),
        Expert(
            id = "exp_3",
            name = "Adv. Bambang Sukoco, S.H., LL.M.",
            title = "Praktisi Hukum Bisnis & Ketenagakerjaan",
            specialization = "Kontrak Kerja, PHK, Pesangon & HKI",
            rating = 4.95f,
            totalConsultations = 310,
            isOnline = true
        ),
        Expert(
            id = "exp_4",
            name = "Siti Rahmawati, S.H.",
            title = "Paralegal Posbakum LBH Jakarta",
            specialization = "Bantuan Hukum Cuma-Cuma (Pro Bono)",
            rating = 4.75f,
            totalConsultations = 95,
            isOnline = false
        )
    )

    fun getExperts(): List<Expert> = expertsList

    fun getExpertById(id: String): Expert? = expertsList.find { it.id == id }
}
