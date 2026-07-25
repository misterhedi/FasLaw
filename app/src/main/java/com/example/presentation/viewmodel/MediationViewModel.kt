package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.model.CertifiedMediator
import com.example.domain.model.DocumentReviewStatus
import com.example.domain.model.MediationDocument
import com.example.domain.model.MediationRequest
import com.example.domain.model.MediationStatusStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediationViewModel : ViewModel() {

    val certifiedMediators = listOf(
        CertifiedMediator(
            id = "MED-001",
            name = "Dr. H. Bambang Widjojanto, S.H., M.H., C.Me.",
            title = "Mediator Bersertifikat Mahkamah Agung RI",
            certificationNumber = "MA-MED-2023-882",
            specialization = "Pertanahan, Sengketa Lahan & Batas SHM",
            rating = 4.9f,
            casesResolved = 124,
            availableSlots = listOf("Senin, 28 Jul (10:00 WIB)", "Selasa, 29 Jul (14:00 WIB)", "Rabu, 30 Jul (09:00 WIB)")
        ),
        CertifiedMediator(
            id = "MED-002",
            name = "Siti Nurjanah, S.H., M.Kn., C.Me.",
            title = "Mediator Spesialis Perdata & Waris Keluarga",
            certificationNumber = "MA-MED-2024-104",
            specialization = "Sengketa Waris, Hibah & Hukum Keluarga",
            rating = 4.8f,
            casesResolved = 98,
            availableSlots = listOf("Senin, 28 Jul (13:30 WIB)", "Kamis, 31 Jul (10:00 WIB)", "Jumat, 1 Ags (15:00 WIB)")
        ),
        CertifiedMediator(
            id = "MED-003",
            name = "Ahmad Rivai, S.H., LL.M., C.Me.",
            title = "Mediator Perdata Bisnis & Ketenagakerjaan",
            certificationNumber = "MA-MED-2022-452",
            specialization = "Utang Piutang, Wanprestasi & Kontrak Bisnis",
            rating = 4.9f,
            casesResolved = 156,
            availableSlots = listOf("Selasa, 29 Jul (11:00 WIB)", "Rabu, 30 Jul (15:30 WIB)", "Jumat, 1 Ags (09:30 WIB)")
        )
    )

    private val initialRequests = listOf(
        MediationRequest(
            id = "MED-2026-801",
            disputeTitle = "Mediasi Sengketa Batas Lahan & SHM No. 402",
            category = "Sengketa Pertanahan / Sertifikat",
            applicantName = "Hasyim (Anda)",
            applicantPhone = "0812-9876-5432",
            opponentName = "Bpk. Hendra Wijaya",
            opponentPhone = "0813-1122-3344",
            opponentRelationship = "Pemilik Lahan Berdampingan",
            description = "Penetapan batas patok tanah antara SHM 402 dan SHM 403 di Serang Barat yang overlapping 1.5 meter.",
            scheduledDate = "Senin, 28 Juli 2026",
            scheduledTime = "10:00 WIB",
            mediator = certifiedMediators[0],
            currentStep = MediationStatusStep.MEDIATOR_REVIEW,
            documents = listOf(
                MediationDocument("DOC-01", "Sertifikat_Hak_Milik_No402.pdf", "Bukti Kepemilikan Lahan", "20 Jul 2026", DocumentReviewStatus.VERIFIED, "Diabsahkan sesuai data BPN"),
                MediationDocument("DOC-02", "Peta_Bidang_Tanah_BPN.pdf", "Peta Ukur BPN", "20 Jul 2026", DocumentReviewStatus.VERIFIED, "Kesesuaian koordinat valid"),
                MediationDocument("DOC-03", "Surat_Teguran_Batas_Lahan.pdf", "Dokumen Kronologi", "21 Jul 2026", DocumentReviewStatus.IN_REVIEW, "Sedang diverifikasi oleh mediator")
            ),
            createdDate = "20 Juli 2026"
        ),
        MediationRequest(
            id = "MED-2026-752",
            disputeTitle = "Mediasi Pembagian Harta Perusahaan Keluarga",
            category = "Sengketa Waris & Keluarga",
            applicantName = "Hasyim (Anda)",
            applicantPhone = "0812-9876-5432",
            opponentName = "Ibu Siska Amelia",
            opponentPhone = "0856-7788-9900",
            opponentRelationship = "Ahli Waris / Saudara Kandung",
            description = "Musyawarah mufakat pembagian aset ruko dan badan usaha pasca pembagian waris tanpa jalur pengadilan.",
            scheduledDate = "Rabu, 30 Juli 2026",
            scheduledTime = "14:00 WIB",
            mediator = certifiedMediators[1],
            currentStep = MediationStatusStep.SESSION_SCHEDULED,
            documents = listOf(
                MediationDocument("DOC-11", "Surat_Kematian_Pewaris.pdf", "Akta Kematian", "15 Jul 2026", DocumentReviewStatus.VERIFIED),
                MediationDocument("DOC-12", "Daftar_Aset_Ruko_Usaha.pdf", "Invetaris Aset", "15 Jul 2026", DocumentReviewStatus.VERIFIED),
                MediationDocument("DOC-13", "KTP_Seluruh_Ahli_Waris.pdf", "Identitas Pihak", "16 Jul 2026", DocumentReviewStatus.VERIFIED)
            ),
            createdDate = "15 Juli 2026"
        )
    )

    private val _requests = MutableStateFlow(initialRequests)
    val requests: StateFlow<List<MediationRequest>> = _requests.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: Form Permohonan, 1: Status Tracker
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedRequest = MutableStateFlow<MediationRequest?>(initialRequests.firstOrNull())
    val selectedRequest: StateFlow<MediationRequest?> = _selectedRequest.asStateFlow()

    fun setSelectedTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun setSelectedRequest(request: MediationRequest) {
        _selectedRequest.value = request
    }

    fun submitNewMediationRequest(
        disputeTitle: String,
        category: String,
        applicantName: String,
        applicantPhone: String,
        opponentName: String,
        opponentPhone: String,
        opponentRelationship: String,
        description: String,
        scheduledDate: String,
        scheduledTime: String,
        selectedMediator: CertifiedMediator,
        initialDocName: String?
    ): MediationRequest {
        val randomCode = (100..999).random()
        val requestId = "MED-2026-$randomCode"

        val initialDocs = mutableListOf<MediationDocument>()
        if (!initialDocName.isNull_or_empty()) {
            initialDocs.add(
                MediationDocument(
                    id = "DOC-${(10..99).random()}",
                    name = initialDocName,
                    type = "Bukti Dokumen Pendukung",
                    uploadDate = "Hari Ini",
                    status = DocumentReviewStatus.IN_REVIEW,
                    note = "Sedang diperiksa oleh tim verifikator LBH"
                )
            )
        } else {
            initialDocs.add(
                MediationDocument(
                    id = "DOC-${(10..99).random()}",
                    name = "Bukti_Kronologi_Sengketa.pdf",
                    type = "Uraian Fakta Hukum",
                    uploadDate = "Hari Ini",
                    status = DocumentReviewStatus.IN_REVIEW,
                    note = "Berkas awal terunggah secara otomatis"
                )
            )
        }

        val newRequest = MediationRequest(
            id = requestId,
            disputeTitle = disputeTitle.ifBlank { "Pengajuan Mediasi $category" },
            category = category,
            applicantName = applicantName.ifBlank { "Hasyim (Anda)" },
            applicantPhone = applicantPhone.ifBlank { "0812-9876-5432" },
            opponentName = opponentName,
            opponentPhone = opponentPhone,
            opponentRelationship = opponentRelationship,
            description = description,
            scheduledDate = scheduledDate,
            scheduledTime = scheduledTime,
            mediator = selectedMediator,
            currentStep = MediationStatusStep.SUBMITTED,
            documents = initialDocs,
            createdDate = "Hari ini"
        )

        _requests.value = listOf(newRequest) + _requests.value
        _selectedRequest.value = newRequest
        _selectedTab.value = 1 // Auto switch to status tracker tab
        return newRequest
    }

    fun addDocumentToSelectedRequest(docName: String, docType: String) {
        val current = _selectedRequest.value ?: return
        val newDoc = MediationDocument(
            id = "DOC-${(100..999).random()}",
            name = docName,
            type = docType,
            uploadDate = "Hari ini",
            status = DocumentReviewStatus.IN_REVIEW,
            note = "Dokumen baru terunggah dan dalam antrean verifikasi"
        )

        val updatedDocs = current.documents + newDoc
        val updatedRequest = current.copy(documents = updatedDocs)

        _selectedRequest.value = updatedRequest
        _requests.value = _requests.value.map { if (it.id == current.id) updatedRequest else it }
    }
}

private fun String?.isNull_or_empty(): Boolean = this == null || this.trim().isEmpty()
