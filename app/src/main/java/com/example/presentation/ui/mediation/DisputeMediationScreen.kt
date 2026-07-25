package com.example.presentation.ui.mediation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.CertifiedMediator
import com.example.domain.model.DocumentReviewStatus
import com.example.domain.model.MediationDocument
import com.example.domain.model.MediationRequest
import com.example.domain.model.MediationStatusStep
import com.example.presentation.viewmodel.MediationViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisputeMediationScreen(
    viewModel: MediationViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToChatMediator: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val requests by viewModel.requests.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val selectedRequest by viewModel.selectedRequest.collectAsState()
    val certifiedMediators = viewModel.certifiedMediators

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showAddDocDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Mediasi Sengketa Online",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Penyelesaian Damai • Mediator MA RI",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = GoldAccent
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF111318))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("dispute_mediation_screen")
        ) {
            // TAB ROW NAVIGATION
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = GoldAccent,
                        height = 3.dp
                    )
                },
                modifier = Modifier.fillMaxWidth().testTag("mediation_tab_row")
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { viewModel.setSelectedTab(0) },
                    text = {
                        Text(
                            text = "Jadwalkan Mediasi",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    },
                    modifier = Modifier.testTag("tab_request_mediation")
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = { viewModel.setSelectedTab(1) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Status & Review Dokumen",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                            if (requests.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(GoldAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${requests.size}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.testTag("tab_status_tracker")
                )
            }

            // TAB CONTENTS
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (selectedTab == 0) {
                    MediationSchedulingTabContent(
                        certifiedMediators = certifiedMediators,
                        onSubmit = { disputeTitle, category, applicantName, applicantPhone, opponentName, opponentPhone, opponentRel, desc, date, time, mediator, docName ->
                            val newReq = viewModel.submitNewMediationRequest(
                                disputeTitle, category, applicantName, applicantPhone,
                                opponentName, opponentPhone, opponentRel, desc,
                                date, time, mediator, docName
                            )
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "Permohonan Mediasi ${newReq.id} Berhasil Dikirim! Diteruskan ke Mediator Bersertifikat."
                                )
                            }
                        }
                    )
                } else {
                    MediationStatusTrackerTabContent(
                        requests = requests,
                        selectedRequest = selectedRequest,
                        onSelectRequest = { viewModel.setSelectedRequest(it) },
                        onAddDocumentClick = { showAddDocDialog = true },
                        onOpenMeeting = { url ->
                            openVirtualMeetingUrl(context, url ?: "https://meet.jit.si/FasLaw-Mediasi-Online")
                        },
                        onChatMediator = { req ->
                            onNavigateToChatMediator(req.mediator.id)
                        }
                    )
                }
            }
        }
    }

    // Add Document Dialog
    if (showAddDocDialog && selectedRequest != null) {
        AddDocumentDialog(
            onDismiss = { showAddDocDialog = false },
            onConfirm = { docName, docType ->
                viewModel.addDocumentToSelectedRequest(docName, docType)
                showAddDocDialog = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Dokumen '$docName' berhasil diunggah untuk diteliti verifikator.")
                }
            }
        )
    }
}

@Composable
private fun MediationSchedulingTabContent(
    certifiedMediators: List<CertifiedMediator>,
    onSubmit: (
        disputeTitle: String,
        category: String,
        applicantName: String,
        applicantPhone: String,
        opponentName: String,
        opponentPhone: String,
        opponentRel: String,
        description: String,
        scheduledDate: String,
        scheduledTime: String,
        selectedMediator: CertifiedMediator,
        initialDocName: String?
    ) -> Unit
) {
    val categories = listOf(
        "Sengketa Pertanahan / Sertifikat",
        "Sengketa Waris & Keluarga",
        "Utang Piutang / Kontrak Bisnis",
        "Ketenagakerjaan & PHK",
        "Sengketa Perdata Umum"
    )

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var disputeTitle by remember { mutableStateOf("") }
    var applicantName by remember { mutableStateOf("Hasyim (Anda)") }
    var applicantPhone by remember { mutableStateOf("0812-9876-5432") }
    var opponentName by remember { mutableStateOf("") }
    var opponentPhone by remember { mutableStateOf("") }
    var opponentRel by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedMediator by remember { mutableStateOf(certifiedMediators[0]) }
    var selectedSlot by remember { mutableStateOf(certifiedMediators[0].availableSlots.firstOrNull() ?: "Senin, 28 Jul (10:00 WIB)") }
    var initialDocName by remember { mutableStateOf("Sertifikat_Lahan_Bukti.pdf") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("mediation_scheduling_form")
    ) {
        // Hero Illustration Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .testTag("mediation_hero_banner"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2430))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_mediation_banner_1784977886723),
                    contentDescription = "Mediasi Sengketa",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent)
                            )
                        )
                        .padding(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxWidth(0.75f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Layanan Resmi Posbakum",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = GoldAccent)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Selesaikan Sengketa Tanpa Jalur Pengadilan",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Hemat waktu & biaya dengan kekuatan hukum Akta Perdamaian",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = Color(0xFFD1E4FF)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 1: Category & Dispute Title
        Text(
            text = "1. Kategori & Pokok Sengketa",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().testTag("category_filter_chips")
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category, style = MaterialTheme.typography.labelMedium) },
                    leadingIcon = if (selectedCategory == category) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = disputeTitle,
            onValueChange = { disputeTitle = it },
            label = { Text("Judul Permohonan Mediasi") },
            placeholder = { Text("Contoh: Sengketa Batas Lahan SHM No. 402 Serang") },
            modifier = Modifier.fillMaxWidth().testTag("dispute_title_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 2: Opponent / Second Party Info
        Text(
            text = "2. Informasi Pihak II (Pihak Lawan / Termohon)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                OutlinedTextField(
                    value = opponentName,
                    onValueChange = { opponentName = it },
                    label = { Text("Nama Lengkap Pihak II") },
                    placeholder = { Text("Bpk. Hendra Wijaya") },
                    modifier = Modifier.fillMaxWidth().testTag("opponent_name_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = opponentPhone,
                        onValueChange = { opponentPhone = it },
                        label = { Text("No. WhatsApp / HP") },
                        placeholder = { Text("0813-XXXX-XXXX") },
                        modifier = Modifier.weight(1f).testTag("opponent_phone_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = opponentRel,
                        onValueChange = { opponentRel = it },
                        label = { Text("Hubungan Pihak") },
                        placeholder = { Text("Pemilik Lahan / Tetangga") },
                        modifier = Modifier.weight(1f).testTag("opponent_rel_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Uraian Singkat Kronologi Sengketa") },
                    placeholder = { Text("Jelaskan permasalahan, batas lahan, nilai klaim, atau pasal pertentangan secara singkat...") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("dispute_desc_input"),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 4
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 3: Select Certified Mediator
        Text(
            text = "3. Pilih Mediator Bersertifikat Mahkamah Agung",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            certifiedMediators.forEach { mediator ->
                val isSelected = selectedMediator.id == mediator.id
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, GoldAccent) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedMediator = mediator
                            selectedSlot = mediator.availableSlots.firstOrNull() ?: ""
                        }
                        .testTag("mediator_card_${mediator.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                selectedMediator = mediator
                                selectedSlot = mediator.availableSlots.firstOrNull() ?: ""
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = GoldAccent)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(NavyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = mediator.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(14.dp))
                            }
                            Text(
                                text = "${mediator.title} • No: ${mediator.certificationNumber}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Spesialisasi: ${mediator.specialization}",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldAccent
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 4: Schedule Session Picker
        Text(
            text = "4. Pilih Slot Waktu Mediasi Virtual",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().testTag("schedule_slot_chips")
        ) {
            items(selectedMediator.availableSlots) { slot ->
                FilterChip(
                    selected = selectedSlot == slot,
                    onClick = { selectedSlot = slot },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EventAvailable, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(slot, style = MaterialTheme.typography.labelMedium)
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldAccent,
                        selectedLabelColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 5: Initial Document Upload
        Text(
            text = "5. Unggah Berkas Bukti Awal",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.AttachFile, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = initialDocName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Terlampir untuk verifikasi kelayakan berkas LBH",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        initialDocName = "Sertifikat_SHM_Revisi_${(10..99).random()}.pdf"
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("change_doc_btn")
                ) {
                    Text("Ganti", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SUBMIT ACTION BUTTON
        Button(
            onClick = {
                val parts = selectedSlot.split("(")
                val datePart = parts.getOrNull(0)?.trim() ?: "Senin, 28 Juli 2026"
                val timePart = parts.getOrNull(1)?.replace(")", "")?.trim() ?: "10:00 WIB"

                onSubmit(
                    disputeTitle,
                    selectedCategory,
                    applicantName,
                    applicantPhone,
                    opponentName.ifBlank { "Bpk. Hendra Wijaya" },
                    opponentPhone.ifBlank { "0813-1122-3344" },
                    opponentRel.ifBlank { "Pemilik Lahan Berdampingan" },
                    description.ifBlank { "Pengajuan mediasi sengketa pertanahan dan batas sertifikat." },
                    datePart,
                    timePart,
                    selectedMediator,
                    initialDocName
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_mediation_request_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
        ) {
            Icon(Icons.Default.Handshake, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Kirim Permohonan & Jadwalkan Mediasi",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun MediationStatusTrackerTabContent(
    requests: List<MediationRequest>,
    selectedRequest: MediationRequest?,
    onSelectRequest: (MediationRequest) -> Unit,
    onAddDocumentClick: () -> Unit,
    onOpenMeeting: (String?) -> Unit,
    onChatMediator: (MediationRequest) -> Unit
) {
    if (requests.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Belum ada pengajuan mediasi aktif.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val currentReq = selectedRequest ?: requests.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("mediation_status_tracker_content")
    ) {
        // SELECT REQUEST CAROUSEL CHIPS
        Text(
            text = "Pilih Permohonan Mediasi Sengketa",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().testTag("request_selector_chips")
        ) {
            items(requests, key = { it.id }) { req ->
                val isSelected = currentReq.id == req.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectRequest(req) },
                    label = {
                        Column {
                            Text(req.id, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                            Text(req.disputeTitle.take(18) + "...", style = MaterialTheme.typography.labelSmall)
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NavyPrimary,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // STATUS STEP PIPELINE TRACKER CARD
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("mediation_step_tracker_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = currentReq.disputeTitle,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "No Tiket: ${currentReq.id} • ${currentReq.category}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(GoldAccent.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Langkah ${currentReq.currentStep.stepNumber}/5",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = GoldAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stepper Visual Progress Nodes
                val allSteps = MediationStatusStep.values()
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    allSteps.forEach { step ->
                        val isDone = step.stepNumber < currentReq.currentStep.stepNumber
                        val isCurrent = step.stepNumber == currentReq.currentStep.stepNumber

                        Row(verticalAlignment = Alignment.Top) {
                            // Step Node Icon
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isDone -> Color(0xFF22C55E)
                                            isCurrent -> GoldAccent
                                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                } else {
                                    Text(
                                        text = "${step.stepNumber}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCurrent) Color.Black else Color.Gray
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = step.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isCurrent || isDone) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                )
                                Text(
                                    text = step.description,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ASSIGNED MEDIATOR & MEETING SCHEDULE CARD
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("assigned_mediator_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(GoldAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.Black, modifier = Modifier.size(22.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentReq.mediator.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "${currentReq.mediator.title} (${currentReq.mediator.certificationNumber})",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color(0xFFD1E4FF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F172A))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Jadwal Mediasi Virtual",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "${currentReq.scheduledDate} • ${currentReq.scheduledTime}",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onChatMediator(currentReq) },
                        modifier = Modifier.weight(1f).height(40.dp).testTag("chat_mediator_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pesan", style = MaterialTheme.typography.labelMedium)
                    }

                    Button(
                        onClick = { onOpenMeeting(currentReq.meetingUrl) },
                        modifier = Modifier.weight(1.3f).height(40.dp).testTag("enter_video_meeting_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                    ) {
                        Icon(Icons.Default.VideoCall, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Masuk Mediasi", style = MaterialTheme.typography.labelMedium, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // DOCUMENT REVIEW CHECKLIST SECTION
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dokumen & Verifikasi Berkas (${currentReq.documents.size})",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            TextButton(
                onClick = onAddDocumentClick,
                modifier = Modifier.testTag("add_document_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tambah Berkas", style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            currentReq.documents.forEach { doc ->
                DocumentReviewItemCard(doc = doc)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // FINAL SETTLEMENT AGREEMENT RESUME CARD
        if (currentReq.currentStep == MediationStatusStep.SESSION_SCHEDULED || currentReq.currentStep == MediationStatusStep.AGREEMENT_DRAFTED) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
                modifier = Modifier.fillMaxWidth().testTag("agreement_draft_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Draf Akta Perdamaian Terbit",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Mediator telah menyusun draf kesepakatan awal berdasarkan musyawarah para pihak. Akta ini mengikat secara hukum sesuai Perma No. 1 Tahun 2016.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFA7F3D0)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Unduh Draf Akta Perdamaian (PDF)", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun DocumentReviewItemCard(doc: MediationDocument) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().testTag("doc_item_${doc.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = doc.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${doc.type} • Unggah: ${doc.uploadDate}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Status Badge
                val (badgeColor, textColor, icon) = when (doc.status) {
                    DocumentReviewStatus.VERIFIED -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), Icons.Default.CheckCircle)
                    DocumentReviewStatus.IN_REVIEW -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), Icons.Default.HourglassTop)
                    DocumentReviewStatus.ACTION_REQUIRED -> Triple(Color(0xFFFEE2E2), Color(0xFFB91C1C), Icons.Default.Info)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = doc.status.label,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = textColor
                        )
                    }
                }
            }

            if (!doc.note.isNull_or_empty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Catatan Verifikator: ${doc.note}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun AddDocumentDialog(
    onDismiss: () -> Unit,
    onConfirm: (docName: String, docType: String) -> Unit
) {
    var docName by remember { mutableStateOf("Surat_Perjanjian_Sengketa_${(10..99).random()}.pdf") }
    var docType by remember { mutableStateOf("Bukti Perjanjian / Kontrak") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Unggah Dokumen Pendukung Baru") },
        text = {
            Column {
                Text(
                    text = "Lampirkan bukti tambahan seperti sertifikat, surat perjanjian, KTP, atau bukti transfer untuk mempercepat mediasi.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = docName,
                    onValueChange = { docName = it },
                    label = { Text("Nama File") },
                    modifier = Modifier.fillMaxWidth().testTag("add_doc_name_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = docType,
                    onValueChange = { docType = it },
                    label = { Text("Jenis / Klasifikasi Dokumen") },
                    modifier = Modifier.fillMaxWidth().testTag("add_doc_type_input"),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(docName, docType) },
                modifier = Modifier.testTag("confirm_upload_doc_btn")
            ) {
                Text("Unggah Dokumen")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

private fun openVirtualMeetingUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun String?.isNull_or_empty(): Boolean = this == null || this.trim().isEmpty()
