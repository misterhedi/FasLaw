package com.example.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kebijakan Privasi",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .testTag("privacy_policy_screen")
        ) {
            // Header Banner Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Jaminan Perlindungan Data Privasi",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "FasLaw menerapkan standar keamanan ISO/IEC 27001. Kami menghormati privasi Anda dan secara ketat TIDAK MENGUMPULKAN dokumen identitas resmi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 1: Data Yang DIKUMPULKAN
            Text(
                text = "1. Data Pengguna Yang Dikumpulkan",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Untuk memberikan layanan konsultasi hukum dan pendampingan pro bono yang efektif, kami hanya mengumpulkan data esensial berikut:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CollectedDataItem(
                        title = "Nama Pengguna",
                        detail = "Digunakan untuk sapaan dan identifikasi saat berkonsultasi dengan tim ahli hukum."
                    )
                    CollectedDataItem(
                        title = "Nomor Telepon / WhatsApp",
                        detail = "Digunakan untuk konfirmasi jadwal konsultasi, kirim kode OTP, dan tindak lanjut bantuan pro bono."
                    )
                    CollectedDataItem(
                        title = "Alamat Email",
                        detail = "Digunakan untuk verifikasi akun, kirim salinan resume hukum, dan pemberitahuan penting."
                    )
                    CollectedDataItem(
                        title = "Ringkasan / Kronologi Kasus Hukum",
                        detail = "Deskripsi teks yang Anda masukkan secara sukarela untuk dianalisis oleh AI atau Advokat."
                    )
                    CollectedDataItem(
                        title = "Dokumen Kasus Terunggah",
                        detail = "Dokumen pendukung (surat kerja, kontrak, somasi) yang Anda unggah secara eksplisit untuk dianalisis."
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Data Yang TIDAK DIKUMPULKAN (Strict Exclusions)
            Text(
                text = "2. Data Sensitif Yang TIDAK Pernah Dikumpulkan",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2C1517)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().testTag("strictly_not_collected_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFFF87171),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Garansi Kerahasiaan & Tanpa Identitas Resmi",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFFCA5A5)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "FasLaw secara eksplisit TIDAK meminta, menyimpan, atau memproses informasi sensitif pemerintah berikut:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFECDD3)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NotCollectedDataItem(
                        title = "TIDAK ADA NIK (Nomor Induk Kependudukan)",
                        detail = "Kami tidak pernah meminta atau menyimpan 16 digit NIK Anda."
                    )
                    NotCollectedDataItem(
                        title = "TIDAK ADA Foto / Pemindaian KTP",
                        detail = "Aplikasi FasLaw bebas dari syarat pengunggahan e-KTP atau Kartu Keluarga."
                    )
                    NotCollectedDataItem(
                        title = "TIDAK ADA Dokumen Identitas Resmi Lainnya",
                        detail = "Paspor, SIM, NPWP, atau identitas sipil tidak diperlukan untuk akses layanan."
                    )
                    NotCollectedDataItem(
                        title = "TIDAK ADA Data Keuangan Sensitif",
                        detail = "Kami tidak menyimpan nomor rekening bank, PIN, atau data kartu kredit."
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 3: Keamanan & Hak Pengguna
            Text(
                text = "3. Keamanan Data & Hak Pengguna",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "• Enkripsi End-to-End: Seluruh lalu lintas data konsultasi dienkripsi menggunakan protokol SSL/TLS modern.\n\n" +
                                "• Hak Penghapusan Data: Anda berhak meminta penghapusan permanen riwayat percakapan dan profil Anda kapan saja melalui Pengaturan Akun atau menghubungi bantuan.\n\n" +
                                "• Non-Komersialisasi Data: FasLaw tidak pernah memperjualbelikan atau membagikan data Anda kepada pihak ketiga untuk kepentingan pemasaran.",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 4: Kontak Tim Privasi
            Text(
                text = "4. Kontak & Pertanyaan Privasi",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Jika Anda memiliki pertanyaan mengenai Kebijakan Privasi ini atau ingin mengajukan permohonan terkait data pribadi Anda, silakan hubungi Tim Privasi Data FasLaw melalui email:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "privacy@faslaw.co.id",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CollectedDataItem(title: String, detail: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF22C55E),
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NotCollectedDataItem(title: String, detail: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.DoNotDisturbOn,
            contentDescription = null,
            tint = Color(0xFFEF4444),
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFFEE2E2)
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFECDD3)
            )
        }
    }
}
