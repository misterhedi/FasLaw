package com.example.domain.model

enum class DocumentReviewStatus(val label: String) {
    VERIFIED("Terverifikasi"),
    IN_REVIEW("Sedang Diteliti"),
    ACTION_REQUIRED("Perlu Perbaikan / Kelengkapan")
}

data class MediationDocument(
    val id: String,
    val name: String,
    val type: String, // e.g. "Sertifikat Lahan / Hak Milik", "Surat Perjanjian / Kontrak", "KTP Pemohon"
    val uploadDate: String,
    val status: DocumentReviewStatus,
    val note: String? = null
)

enum class MediationStatusStep(
    val stepNumber: Int,
    val title: String,
    val description: String
) {
    SUBMITTED(1, "Pengajuan Diterima", "Permohonan mediasi telah masuk ke sistem dan menunggu verifikasi awal"),
    DOCUMENT_VERIFICATION(2, "Verifikasi Dokumen & Pihak", "Tim legal memeriksa keabsahan sertifikat & bukti berkas pendukung"),
    MEDIATOR_REVIEW(3, "Review Mediator Bersertifikat", "Mediator mempelajari pokok sengketa & menghubungi para pihak"),
    SESSION_SCHEDULED(4, "Sesi Mediasi Online Terjadwal", "Ruang virtual Zoom/Meet telah disiapkan untuk negosiasi damai"),
    AGREEMENT_DRAFTED(5, "Draf Akta Perdamaian", "Kesepakatan akhir disahkan dalam akta perdamaian berikatan hukum")
}

data class CertifiedMediator(
    val id: String,
    val name: String,
    val title: String, // e.g. "Mediator Bersertifikat Mahkamah Agung (C.Me)"
    val certificationNumber: String, // e.g. "MA-MED-2023-882"
    val specialization: String, // e.g. "Pertanahan & Perdata Bisnis"
    val rating: Float, // e.g. 4.9f
    val casesResolved: Int, // e.g. 84
    val availableSlots: List<String> // e.g. ["Besok, 10:00 WIB", "28 Jul, 14:00 WIB", "29 Jul, 09:00 WIB"]
)

data class MediationRequest(
    val id: String, // e.g. "MED-2026-789"
    val disputeTitle: String,
    val category: String, // e.g. "Sengketa Pertanahan / Sertifikat", "Sengketa Waris & Keluarga", "Utang Piutang / Kontrak", "Ketenagakerjaan"
    val applicantName: String,
    val applicantPhone: String,
    val opponentName: String,
    val opponentPhone: String,
    val opponentRelationship: String, // e.g. "Pemilik Lahan Berdampingan", "Rekan Bisnis / Debitur", "Ahli Waris Pasangan"
    val description: String,
    val scheduledDate: String, // e.g. "28 Juli 2026"
    val scheduledTime: String, // e.g. "10:00 WIB"
    val mediator: CertifiedMediator,
    val currentStep: MediationStatusStep,
    val documents: List<MediationDocument>,
    val createdDate: String,
    val meetingUrl: String? = "https://meet.jit.si/FasLaw-Mediasi-Online"
)
