package com.example.presentation.ui.probono

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.ProBonoApplication
import com.example.presentation.ui.components.LegalDisclaimerFooter
import com.example.presentation.viewmodel.AuthViewModel
import com.example.presentation.viewmodel.ProBonoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProBonoRegistrationScreen(
    proBonoViewModel: ProBonoViewModel,
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAiChat: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    var currentStep by remember { mutableIntStateOf(1) }
    var submittedTicket by remember { mutableStateOf<ProBonoApplication?>(null) }

    // Form States
    var selectedCategory by remember { mutableStateOf("Ketenagakerjaan (PHK/Pesangon)") }
    var caseTitle by remember { mutableStateOf("") }
    var caseSummary by remember { mutableStateOf("") }
    var selectedUrgency by remember { mutableStateOf("Penting") }

    var financialDeclaration by remember { mutableStateOf("Di bawah Rp 3.000.000 / Bulan (UMP/Kena PHK)") }
    var isSelfDeclaredVerified by remember { mutableStateOf(false) }

    var applicantName by remember { mutableStateOf(currentUser?.name ?: "Budi Santoso") }
    var applicantPhone by remember { mutableStateOf(currentUser?.phone ?: "081234567890") }
    var applicantEmail by remember { mutableStateOf(currentUser?.email ?: "budi.santoso@email.com") }
    var applicantCity by remember { mutableStateOf("Jakarta Selatan") }
    var preferredContactMethod by remember { mutableStateOf("WhatsApp & Panggilan Suara") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        "Ketenagakerjaan (PHK/Pesangon)",
        "Pidana / Perlindungan Hukum",
        "Perdata / Utang / Kontrak",
        "Pertanahan & Sengketa Lahan",
        "Keluarga / KDRT / Hak Asuh",
        "Sengketa Konsumen & Cyber"
    )

    val urgencies = listOf("Biasa", "Penting", "Sangat Mendesak (Emergency)")

    val financialOptions = listOf(
        "Di bawah Rp 3.000.000 / Bulan (UMP/Kena PHK)",
        "Pekerja Lepas / Tidak Tetap / Tanpa Penghasilan Rutin",
        "Pensiunan / Lansia / Ibu Rumah Tangga / Pelajar",
        "Kondisi Ekonomi Menengah-Bawah Membutuhkan Pendampingan"
    )

    val contactMethods = listOf(
        "WhatsApp & Panggilan Suara",
        "Panggilan Suara Saja",
        "Tatap Muka di Kantor LBH / Posbakum"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (submittedTicket != null) "Bukti Pendaftaran Pro Bono" else "Pendaftaran Pro Bono",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE2E2E6)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (submittedTicket != null) {
                                onNavigateBack()
                            } else if (currentStep > 1) {
                                currentStep -= 1
                            } else {
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color(0xFFE2E2E6)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF111318))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            LegalDisclaimerFooter(modifier = Modifier.padding(bottom = 12.dp))
            if (submittedTicket != null) {
                // Success View
                ProBonoTicketSuccessView(
                    ticket = submittedTicket!!,
                    onNavigateToAiChat = onNavigateToAiChat,
                    onFinish = onNavigateBack
                )
            } else {
                // Step Indicator Progress Bar
                StepProgressBar(currentStep = currentStep, totalSteps = 4)

                Spacer(modifier = Modifier.height(16.dp))

                // Mandatory No-NIK / Privacy Guarantee Banner
                PrivacyGuaranteeCard()

                Spacer(modifier = Modifier.height(16.dp))

                when (currentStep) {
                    1 -> {
                        // STEP 1: Detail Masalah Hukum
                        Text(
                            text = "Langkah 1 dari 4: Detail Masalah Hukum",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFA8C7FF)
                        )
                        Text(
                            text = "Pilih kategori dan ceritakan garis besar permasalahan hukum yang Anda hadapi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF909094)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Kategori Permasalahan",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFE2E2E6)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        categories.forEach { cat ->
                            val isSelected = selectedCategory == cat
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { selectedCategory = cat }
                                    .testTag("category_option_${cat.take(10)}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF004A77) else Color(0xFF1A1C1E)
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFA8C7FF)) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedCategory = cat },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFA8C7FF))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = cat,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelected) Color(0xFFD1E4FF) else Color(0xFFE2E2E6)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Judul Masalah",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFE2E2E6)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = caseTitle,
                            onValueChange = { caseTitle = it },
                            label = { Text("Judul Masalah") },
                            placeholder = { Text("Contoh: Pemutusan Hubungan Kerja (PHK) Sehak-pihak") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("probono_title_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA8C7FF),
                                unfocusedBorderColor = Color(0xFF44474E),
                                focusedContainerColor = Color(0xFF1A1C1E),
                                unfocusedContainerColor = Color(0xFF1A1C1E)
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Deskripsi Singkat",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFE2E2E6)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = caseSummary,
                            onValueChange = { caseSummary = it },
                            label = { Text("Deskripsi Singkat") },
                            placeholder = { Text("Jelaskan secara singkat kronologi kejadian, pihak yang terlibat, dan bantuan yang Anda harapkan...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .testTag("probono_summary_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA8C7FF),
                                unfocusedBorderColor = Color(0xFF44474E),
                                focusedContainerColor = Color(0xFF1A1C1E),
                                unfocusedContainerColor = Color(0xFF1A1C1E)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Tingkat Urgensi Kasus",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFE2E2E6)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            urgencies.forEach { urg ->
                                val isUrgSelected = selectedUrgency == urg
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedUrgency = urg }
                                        .testTag("urgency_$urg"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isUrgSelected) Color(0xFF004A77) else Color(0xFF1A1C1E)
                                    ),
                                    border = if (isUrgSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA8C7FF)) else null
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = urg,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = if (isUrgSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 11.sp
                                            ),
                                            color = if (isUrgSelected) Color(0xFFD1E4FF) else Color(0xFFE2E2E6)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // STEP 2: Deklarasi Kemampuan Ekonomi
                        Text(
                            text = "Langkah 2 dari 4: Deklarasi Mandiri Ekonomi",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFA8C7FF)
                        )
                        Text(
                            text = "Layanan Bantuan Hukum Pro Bono diperuntukkan bagi masyarakat kurang mampu secara cuma-cuma tanpa dipungut biaya.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF909094)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Kategori Penghasilan / Kondisi Ekonomi",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFE2E2E6)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        financialOptions.forEach { option ->
                            val isSelected = financialDeclaration == option
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { financialDeclaration = option },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF004A77) else Color(0xFF1A1C1E)
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFA8C7FF)) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { financialDeclaration = option },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFA8C7FF))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelected) Color(0xFFD1E4FF) else Color(0xFFE2E2E6)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF33444C))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .clickable { isSelfDeclaredVerified = !isSelfDeclaredVerified },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelfDeclaredVerified,
                                    onCheckedChange = { isSelfDeclaredVerified = it },
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFA8C7FF)),
                                    modifier = Modifier.testTag("probono_declaration_checkbox")
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Saya menyatakan secara jujur dan sadar bahwa saya membutuhkan bantuan hukum pro bono (cuma-cuma) tanpa perlu melampirkan berkas fisik KTP/SKTM.",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = Color(0xFFE2E2E6)
                                )
                            }
                        }
                    }

                    3 -> {
                        // STEP 3: Kontak & Domisili
                        Text(
                            text = "Langkah 3 dari 4: Kontak & Wilayah Domisili",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFA8C7FF)
                        )
                        Text(
                            text = "Informasi ini digunakan tim LBH untuk menghubungi Anda secara langsung saat pemadanan advokat.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF909094)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = applicantName,
                            onValueChange = { applicantName = it },
                            label = { Text("Nama") },
                            placeholder = { Text("Nama Lengkap") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("probono_name_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA8C7FF),
                                unfocusedBorderColor = Color(0xFF44474E),
                                focusedContainerColor = Color(0xFF1A1C1E),
                                unfocusedContainerColor = Color(0xFF1A1C1E)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = applicantPhone,
                            onValueChange = { applicantPhone = it },
                            label = { Text("Nomor Telepon") },
                            placeholder = { Text("Nomor WhatsApp / HP Active") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("probono_phone_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA8C7FF),
                                unfocusedBorderColor = Color(0xFF44474E),
                                focusedContainerColor = Color(0xFF1A1C1E),
                                unfocusedContainerColor = Color(0xFF1A1C1E)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = applicantEmail,
                            onValueChange = { applicantEmail = it },
                            label = { Text("Email (Opsional)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("probono_email_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA8C7FF),
                                unfocusedBorderColor = Color(0xFF44474E),
                                focusedContainerColor = Color(0xFF1A1C1E),
                                unfocusedContainerColor = Color(0xFF1A1C1E)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = applicantCity,
                            onValueChange = { applicantCity = it },
                            label = { Text("Kota / Kabupaten Domisili") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("probono_city_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA8C7FF),
                                unfocusedBorderColor = Color(0xFF44474E),
                                focusedContainerColor = Color(0xFF1A1C1E),
                                unfocusedContainerColor = Color(0xFF1A1C1E)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Preferensi Kontak Layanan",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFE2E2E6)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        contactMethods.forEach { method ->
                            val isSelected = preferredContactMethod == method
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { preferredContactMethod = method },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF004A77) else Color(0xFF1A1C1E)
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFA8C7FF)) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { preferredContactMethod = method },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFA8C7FF))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = method,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelected) Color(0xFFD1E4FF) else Color(0xFFE2E2E6)
                                    )
                                }
                            }
                        }
                    }

                    4 -> {
                        // STEP 4: Konfirmasi & Review
                        Text(
                            text = "Langkah 4 dari 4: Konfirmasi Permohonan",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFA8C7FF)
                        )
                        Text(
                            text = "Periksa kembali ringkasan permohonan bantuan hukum pro bono Anda sebelum dikirimkan.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF909094)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1C1E)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF44474E))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                ReviewItemRow(label = "Kategori Hukum", value = selectedCategory)
                                ReviewItemRow(label = "Tingkat Urgensi", value = selectedUrgency)
                                ReviewItemRow(
                                    label = "Ringkasan Kasus",
                                    value = caseSummary.ifBlank { "Permohonan pendampingan & konsultasi pro bono" }
                                )
                                ReviewItemRow(label = "Status Deklarasi", value = financialDeclaration)
                                ReviewItemRow(label = "Nama Pemohon", value = applicantName)
                                ReviewItemRow(label = "No. HP / WA", value = applicantPhone)
                                ReviewItemRow(label = "Kota Domisili", value = applicantCity)
                                ReviewItemRow(label = "Metode Kontak", value = preferredContactMethod)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LegalDisclaimerBox()
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Error Banner if validation fails
                errorMessage?.let { msg ->
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFEF4444),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep -= 1 },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Kembali", color = Color(0xFFE2E2E6))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Button(
                        onClick = {
                            errorMessage = null
                            when (currentStep) {
                                1 -> {
                                    if (caseSummary.isBlank() || caseSummary.length < 5) {
                                        errorMessage = "Mohon isi judul dan deskripsi singkat masalah Anda."
                                    } else {
                                        currentStep = 2
                                    }
                                }
                                2 -> {
                                    if (!isSelfDeclaredVerified) {
                                        errorMessage = "Mohon centang persetujuan deklarasi mandiri bantuan pro bono."
                                    } else {
                                        currentStep = 3
                                    }
                                }
                                3 -> {
                                    if (applicantName.isBlank() || applicantPhone.isBlank() || applicantCity.isBlank()) {
                                        errorMessage = "Mohon isi nama, nomor telepon, dan kota domisili Anda."
                                    } else {
                                        currentStep = 4
                                    }
                                }
                                4 -> {
                                    val ticket = proBonoViewModel.submitApplication(
                                        category = selectedCategory,
                                        summary = caseSummary,
                                        urgency = selectedUrgency,
                                        applicantName = applicantName,
                                        phone = applicantPhone,
                                        email = applicantEmail,
                                        city = applicantCity,
                                        financialDeclaration = financialDeclaration,
                                        preferredContact = preferredContactMethod
                                    )
                                    submittedTicket = ticket
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("probono_next_or_submit_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004A77))
                    ) {
                        Text(
                            text = if (currentStep == 4) "Kirim Pengajuan" else "Lanjut",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFD1E4FF)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = if (currentStep == 4) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFFD1E4FF),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepProgressBar(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalSteps) {
            val isActive = i <= currentStep
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(if (isActive) Color(0xFFA8C7FF) else Color(0xFF33444C))
            )
        }
    }
}

@Composable
fun PrivacyGuaranteeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF004A77).copy(alpha = 0.4f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA8C7FF).copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Color(0xFFA8C7FF),
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Jaminan Kerahasiaan & Bebas KTP/NIK",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFD1E4FF)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "FasLaw TIDAK PERNAH meminta upload KTP, NIK, atau identitas pribadi sensitif. Cukup ikuti deklarasi mandiri untuk memadankan kasus Anda ke Lembaga Bantuan Hukum (LBH) terdekat.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE2E2E6).copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun ReviewItemRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF909094)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFE2E2E6)
        )
    }
}

@Composable
fun LegalDisclaimerBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF2D3036))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFA8C7FF),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.legal_disclaimer),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE2E2E6)
            )
        }
    }
}

@Composable
fun ProBonoTicketSuccessView(
    ticket: ProBonoApplication,
    onNavigateToAiChat: () -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFF004A77)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFFD1E4FF),
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pengajuan Pro Bono Berhasil!",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFE2E2E6)
        )

        Text(
            text = "Nomor Tiket Pendaftaran: ${ticket.ticketId}",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFA8C7FF)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1C1E)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF44474E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status Pengajuan",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF909094)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF004A77))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = ticket.status,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFD1E4FF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ReviewItemRow(label = "Unit LBH Dituju", value = ticket.assignedLbhName)
                ReviewItemRow(label = "Kategori Masalah", value = ticket.category)
                ReviewItemRow(label = "Tingkat Urgensi", value = ticket.urgency)
                ReviewItemRow(label = "Nama Pemohon", value = ticket.applicantName)
                ReviewItemRow(label = "Kontak Terdaftar", value = "${ticket.phone} (${ticket.preferredContact})")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // What happens next card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF33444C))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Langkah Selanjutnya:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFD1E4FF)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Tim paralegal / advokat LBH akan meninjau kronologi Anda dalam 1x24 jam.\n2. Advokat akan menghubungi WhatsApp Anda untuk penjadwalan konsultasi pendampingan.\n3. Anda dapat bertanya kepada AI Legal Assistant FasLaw kapan saja mengenai persiapan berkas pendukung.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE2E2E6)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToAiChat,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("probono_ask_ai_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004A77))
        ) {
            Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color(0xFFD1E4FF))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tanya AI Terkait Kasus Ini", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFFD1E4FF))
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("probono_finish_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Kembali ke Beranda", style = MaterialTheme.typography.titleSmall, color = Color(0xFFE2E2E6))
        }
    }
}
