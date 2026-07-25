package com.example.presentation.ui.profile

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.viewmodel.SettingsViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsState()
    val isConsultationAlertsEnabled by viewModel.isConsultationAlertsEnabled.collectAsState()
    val isLegalNewsAlertsEnabled by viewModel.isLegalNewsAlertsEnabled.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val isHighContrastEnabled by viewModel.isHighContrastEnabled.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (appLanguage == "en") "Application Settings" else "Pengaturan Aplikasi",
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .testTag("settings_screen")
        ) {
            // ISO Standards Compliance Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("iso_standards_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessibilityNew,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = if (appLanguage == "en") "ISO/IEC 25010 Accessibility Compliance" else "Aksesibilitas Standar ISO/IEC 25010",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (appLanguage == "en")
                                "FasLaw settings adhere to international accessibility and high contrast visual standards for inclusive legal assistance."
                            else
                                "Pengaturan FasLaw mematuhi standar internasional aksesibilitas dan kontras visual tinggi demi kenyamanan seluruh pengguna.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 1: Appearance & Theme
            SectionHeader(
                title = if (appLanguage == "en") "1. Appearance & Accessibility" else "1. Tampilan & Aksesibilitas"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Dark / Light Mode Switch
                    SettingToggleRow(
                        icon = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                        title = if (appLanguage == "en") "Dark Mode" else "Mode Gelap (Dark Mode)",
                        subtitle = if (isDarkMode)
                            (if (appLanguage == "en") "M3 Eye-Safe Dark Theme Active" else "Tema Gelap M3 Anti-Lelah Mata Active")
                        else
                            (if (appLanguage == "en") "ISO 25010 High-Contrast Light Theme" else "Tema Terang Kontras Tinggi ISO 25010"),
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.setDarkMode(it) },
                        testTag = "settings_dark_mode_switch"
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)))
                    Spacer(modifier = Modifier.height(12.dp))

                    // High Contrast Accessibility Switch
                    SettingToggleRow(
                        icon = Icons.Default.Contrast,
                        title = if (appLanguage == "en") "High Contrast Mode" else "Mode Kontras Tinggi (ISO)",
                        subtitle = if (appLanguage == "en")
                            "Enhance element borders and text contrast for low-vision support"
                        else
                            "Tingkatkan kontras garis batas dan teks untuk kemudahan penglihatan",
                        checked = isHighContrastEnabled,
                        onCheckedChange = { viewModel.setHighContrastEnabled(it) },
                        testTag = "settings_high_contrast_switch"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 2: Notification Preferences
            SectionHeader(
                title = if (appLanguage == "en") "2. Notification Preferences" else "2. Pengaturan Notifikasi & Pemberitahuan"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Global Notifications
                    SettingToggleRow(
                        icon = if (isNotificationsEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                        title = if (appLanguage == "en") "Allow App Notifications" else "Izinkan Notifikasi Aplikasi",
                        subtitle = if (appLanguage == "en") "Receive important updates and reminders" else "Terima pengingat dan pembaruan penting aplikasi",
                        checked = isNotificationsEnabled,
                        onCheckedChange = { viewModel.setNotificationsEnabled(it) },
                        testTag = "settings_notifications_switch"
                    )

                    AnimatedVisibility(visible = isNotificationsEnabled) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)))
                            Spacer(modifier = Modifier.height(12.dp))

                            // Consultation Alerts
                            SettingToggleRow(
                                icon = Icons.Default.Notifications,
                                title = if (appLanguage == "en") "Consultation & Session Reminders" else "Pengingat Sesi Konsultasi AI & Advokat",
                                subtitle = if (appLanguage == "en") "Alerts for appointment schedules and response updates" else "Pemberitahuan jadwal temuduga dan balasan advokat",
                                checked = isConsultationAlertsEnabled,
                                onCheckedChange = { viewModel.setConsultationAlertsEnabled(it) },
                                testTag = "settings_consultation_alerts_switch"
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)))
                            Spacer(modifier = Modifier.height(12.dp))

                            // Legal News Updates
                            SettingToggleRow(
                                icon = Icons.Default.VolumeUp,
                                title = if (appLanguage == "en") "Legal News & Law Updates" else "Kabar Hukum & Artikel Edukasi",
                                subtitle = if (appLanguage == "en") "Weekly summary of national regulations" else "Rangkuman mingguan pasal dan undang-undang terbaru",
                                checked = isLegalNewsAlertsEnabled,
                                onCheckedChange = { viewModel.setLegalNewsAlertsEnabled(it) },
                                testTag = "settings_legal_news_switch"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 3: Application Language
            SectionHeader(
                title = if (appLanguage == "en") "3. Application Language" else "3. Bahasa Aplikasi (Language)"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (appLanguage == "en") "Pilih Bahasa / Select Language" else "Pilih Bahasa Antarmuka",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        LanguageChip(
                            label = "Bahasa Indonesia 🇮🇩",
                            isSelected = appLanguage == "id",
                            onClick = {
                                viewModel.setAppLanguage("id")
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Bahasa disetel ke Bahasa Indonesia.")
                                }
                            },
                            tag = "settings_language_id_chip",
                            modifier = Modifier.weight(1f)
                        )

                        LanguageChip(
                            label = "English (US) 🇺🇸",
                            isSelected = appLanguage == "en",
                            onClick = {
                                viewModel.setAppLanguage("en")
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Language set to English.")
                                }
                            },
                            tag = "settings_language_en_chip",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 4: Storage & Maintenance
            SectionHeader(
                title = if (appLanguage == "en") "4. Cache & Data Storage" else "4. Penyimpanan & Pemeliharaan System"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (appLanguage == "en") "Clear Local Application Cache" else "Bersihkan Cache & Berkas Sementara",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (appLanguage == "en") "Frees up storage without affecting your saved cases (~12.4 MB)" else "Mengosongkan ruang tanpa menghapus data kasus Anda (~12,4 MB)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    if (appLanguage == "en") "App cache successfully cleared (12.4 MB freed)." else "Cache aplikasi berhasil dibersihkan (12,4 MB dibebaskan)."
                                )
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("settings_clear_cache_btn")
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (appLanguage == "en") "Clear" else "Hapus", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun SettingToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag)
        )
    }
}

@Composable
private fun LanguageChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            )
        },
        leadingIcon = if (isSelected) {
            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.testTag(tag)
    )
}
