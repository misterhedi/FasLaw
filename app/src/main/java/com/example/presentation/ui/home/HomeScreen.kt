package com.example.presentation.ui.home

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.presentation.ui.components.FasLawBottomBar
import com.example.presentation.viewmodel.AuthViewModel
import com.example.presentation.viewmodel.KnowledgeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    knowledgeViewModel: KnowledgeViewModel,
    onNavigateToAiChat: () -> Unit,
    onNavigateToExpertList: () -> Unit,
    onNavigateToDocumentAnalysis: () -> Unit,
    onNavigateToLbhLocator: () -> Unit,
    onNavigateToKnowledgeBase: () -> Unit,
    onNavigateToArticleDetail: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToQuickPrompt: (String) -> Unit,
    onNavigateToProBono: () -> Unit = {},
    onNavigateToMediation: () -> Unit = {}
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showSosDialog by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf("Kota Serang, Banten") }
    var showLocationPicker by remember { mutableStateOf(false) }

    val userName = currentUser?.name ?: "Hasyim"

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "FasLaw",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 18.sp
                                )
                            )
                            Text(
                                text = "Free and Smart Law",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = Color(0xFFEF4444),
                                contentColor = Color.White
                            ) {
                                Text("3", fontWeight = FontWeight.Bold)
                            }
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("3 Notifikasi Hukum Baru: Jadwal Konsultasi & Update Regulasi")
                                }
                            }
                            .testTag("notifications_badge_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = onNavigateToKnowledgeBase,
                        modifier = Modifier.testTag("search_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Cari Hukum",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            // SOS Floating Action Button from Mockup
            FloatingActionButton(
                onClick = { showSosDialog = true },
                containerColor = Color(0xFFEF4444),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("sos_floating_button")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Emergency,
                        contentDescription = "SOS Bantuan Darurat",
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "SOS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        },
        bottomBar = {
            FasLawBottomBar(
                currentRoute = "home",
                onNavigate = { route ->
                    when (route) {
                        "ai_chat" -> onNavigateToAiChat()
                        "knowledge_base" -> onNavigateToKnowledgeBase()
                        "document_analysis" -> onNavigateToDocumentAnalysis()
                        "profile" -> onNavigateToProfile()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // 1. WELCOME HEADER & LOCATION
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Selamat Datang,",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$userName 👋",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showLocationPicker = true }
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedLocation,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Court Building Illustration Header Image
                Image(
                    painter = painterResource(id = R.drawable.img_court_header_1784975290375),
                    contentDescription = "Court Building",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. HERO CAROUSEL BANNER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToAiChat() }
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF003882), Color(0xFF0052CC))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1.2f)) {
                            Text(
                                text = "Konsultasi Hukum Gratis\nUntuk Semua",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 22.sp
                                ),
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Dapatkan bantuan hukum dari advokat volunteer terpercaya.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = Color(0xFFD1E4FF)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = onNavigateToAiChat,
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF0052CC)
                                ),
                                modifier = Modifier
                                    .height(36.dp)
                                    .testTag("start_consultation_button")
                            ) {
                                Text(
                                    text = "Mulai Konsultasi",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Image(
                            painter = painterResource(id = R.drawable.img_hero_consultation_1784975258366),
                            contentDescription = "Konsultasi Hukum",
                            modifier = Modifier
                                .weight(0.8f)
                                .height(110.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Carousel Dots Indicator
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.5f)))
                        Box(modifier = Modifier.size(16.dp, 6.dp).clip(CircleShape).background(Color.White))
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.5f)))
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. HIGHLIGHTED 4 QUICK ACTION CARDS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionCardItem(
                    title = "Konsultasi\nGratis",
                    icon = Icons.Default.Gavel,
                    badgeColor = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToAiChat,
                    tag = "action_konsultasi_gratis"
                )
                QuickActionCardItem(
                    title = "AI Assistant\n24/7",
                    icon = Icons.Default.SmartToy,
                    badgeColor = Color(0xFFF3E5F5),
                    iconTint = Color(0xFF7B1FA2),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToAiChat,
                    tag = "action_ai_assistant"
                )
                QuickActionCardItem(
                    title = "Buat Surat\nHukum",
                    icon = Icons.Default.Description,
                    badgeColor = Color(0xFFFFF3E0),
                    iconTint = Color(0xFFE65100),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToDocumentAnalysis,
                    tag = "action_buat_surat"
                )
                QuickActionCardItem(
                    title = "Bantuan\nDarurat",
                    icon = Icons.Default.Emergency,
                    badgeColor = Color(0xFFFFEBEE),
                    iconTint = Color(0xFFC62828),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToProBono,
                    tag = "action_bantuan_darurat"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4. MAIN 8 FEATURE GRID
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Row 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FeatureGridItem(
                            title = "Pro Bono",
                            subtitle = "Bantuan Gratis",
                            icon = Icons.Default.Gavel,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToProBono,
                            tag = "grid_probono"
                        )
                        FeatureGridItem(
                            title = "Advokat",
                            subtitle = "Volunteer",
                            icon = Icons.Default.PersonOutline,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToExpertList,
                            tag = "grid_advokat"
                        )
                        FeatureGridItem(
                            title = "Mediasi",
                            subtitle = "Sengketa",
                            icon = Icons.Default.Handshake,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToMediation,
                            tag = "grid_mediasi"
                        )
                        FeatureGridItem(
                            title = "Peraturan",
                            subtitle = "Terbaru",
                            icon = Icons.Default.MenuBook,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToKnowledgeBase,
                            tag = "grid_peraturan"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Row 2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FeatureGridItem(
                            title = "Edukasi",
                            subtitle = "Hukum",
                            icon = Icons.Default.School,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToKnowledgeBase,
                            tag = "grid_edukasi"
                        )
                        FeatureGridItem(
                            title = "Direktori",
                            subtitle = "Advokat & LBH",
                            icon = Icons.Default.AccountBalance,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToLbhLocator,
                            tag = "grid_direktori"
                        )
                        FeatureGridItem(
                            title = "LBH",
                            subtitle = "Terdekat",
                            icon = Icons.Default.Place,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToLbhLocator,
                            tag = "grid_lbh_terdekat"
                        )
                        FeatureGridItem(
                            title = "Riwayat",
                            subtitle = "Semua Aktivitas",
                            icon = Icons.Default.History,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToDocumentAnalysis,
                            tag = "grid_riwayat"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 5. MIDDLE SECTION - TWO CARDS ROW (AI Legal Assistant & Last Consultation)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 1: AI Legal Assistant
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToAiChat() }
                        .testTag("ai_assistant_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "AI Legal Assistant",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.img_robot_assistant_1784975277636),
                                contentDescription = "Robot AI",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tanyakan semua masalah hukum Anda kapan saja, 24 jam.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, lineHeight = 13.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = onNavigateToAiChat,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Mulai Chat →", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }

                // Card 2: Konsultasi Terakhir
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("last_consultation_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Konsultasi Terakhir",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Lihat Semua",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.clickable { onNavigateToExpertList() }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E2E6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("BS", fontWeight = FontWeight.Bold, color = Color(0xFF003882), fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Konsultasi Wanprestasi",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Dengan: Adv. Budi Santoso",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFFF8E1))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Sedang Berlangsung",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF57F17),
                                    fontSize = 9.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "12 Mei 2024 • 14:30",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 6. EDUKASI TERBARU HORIZONTAL SCROLL
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edukasi Terbaru",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Lihat Semua",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onNavigateToKnowledgeBase() }
                        .testTag("see_all_edukasi_button")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    listOf(
                        EduCardModel("1", "Hak dan Kewajiban Dalam Perjanjian", "⏱ 8:45", "👁 1.2K", "Artikel", Color(0xFF0052CC), R.drawable.img_doc_scanner_1784972865941),
                        EduCardModel("2", "Cara Mengajukan Gugatan Perdata", "⏱ 12:30", "👁 2.1K", "Video", Color(0xFF7B1FA2), R.drawable.img_hero_banner_1784972851467),
                        EduCardModel("3", "Podcast: Mengenal Hukum Ketenagakerjaan", "⏱ 15:20", "👁 980", "Podcast", Color(0xFF2E7D32), R.drawable.img_hero_consultation_1784975258366)
                    )
                ) { edu ->
                    Card(
                        modifier = Modifier
                            .width(200.dp)
                            .clickable { onNavigateToArticleDetail(edu.id) }
                            .testTag("edu_card_${edu.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column {
                            Box {
                                Image(
                                    painter = painterResource(id = edu.imageRes),
                                    contentDescription = edu.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentScale = ContentScale.Crop
                                )

                                // Duration pill badge top left
                                Box(
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Black.copy(alpha = 0.65f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                        .align(Alignment.TopStart)
                                ) {
                                    Text(
                                        text = edu.duration,
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 9.sp)
                                    )
                                }

                                // Bookmark icon top right
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = "Simpan",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .align(Alignment.TopEnd)
                                        .size(18.dp)
                                )
                            }

                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = edu.title,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = edu.views,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(edu.tagColor.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = edu.tagText,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = edu.tagColor,
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 7. BOTTOM ROW: PERATURAN TERBARU & LBH TERDEKAT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Peraturan Terbaru Column
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("peraturan_terbaru_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Peraturan Terbaru",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Lihat Semua",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.clickable { onNavigateToKnowledgeBase() }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        PeraturanMiniRow(tag = "UU", color = Color(0xFF7B1FA2), title = "UU No. 1 Tahun 2023", sub = "KUHP Baru")
                        PeraturanMiniRow(tag = "PP", color = Color(0xFF2E7D32), title = "PP No. 24 Tahun 2024", sub = "Peraturan Pemerintah")
                        PeraturanMiniRow(tag = "PERMA", color = Color(0xFFE65100), title = "PERMA No. 1 Tahun 2024", sub = "Mahkamah Agung")
                    }
                }

                // LBH Terdekat Column
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToLbhLocator() }
                        .testTag("lbh_terdekat_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "LBH Terdekat",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Lihat Semua",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.clickable { onNavigateToLbhLocator() }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Map Preview Thumbnail
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE0E0E0))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_doc_scanner_1784972865941),
                                contentDescription = "Map Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.25f))
                            )
                            Row(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("LBH Kota Serang", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "LBH Kota Serang",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "2,1 km dari lokasi Anda",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Navigasi →",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.clickable { onNavigateToLbhLocator() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // SOS Emergency Modal Dialog
    if (showSosDialog) {
        AlertDialog(
            onDismissRequest = { showSosDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Emergency, contentDescription = null, tint = Color(0xFFEF4444))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Bantuan Darurat Hukum SOS", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    "Layanan darurat pendampingan hukum cepat 24 jam untuk kondisi mencederai hak konstitusional, penangkapan mendesak, atau KDRT.\n\nTidak dipungut biaya dan tanpa persyaratkan dokumen NIK/KTP."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSosDialog = false
                        onNavigateToProBono()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Ajukan Bantuan Darurat Now", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSosDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }

    // Location Picker Modal Dialog
    if (showLocationPicker) {
        AlertDialog(
            onDismissRequest = { showLocationPicker = false },
            title = { Text("Pilih Wilayah Domisili", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    listOf("Kota Serang, Banten", "Jakarta Selatan, DKI Jakarta", "Bandung, Jawa Barat", "Surabaya, Jawa Timur", "Medan, Sumatera Utara").forEach { loc ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedLocation = loc
                                    showLocationPicker = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(loc, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationPicker = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun QuickActionCardItem(
    title: String,
    icon: ImageVector,
    badgeColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    tag: String
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag(tag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 4.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun FeatureGridItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 6.dp)
            .testTag(tag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PeraturanMiniRow(tag: String, color: Color, title: String, sub: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(tag, fontWeight = FontWeight.Bold, color = color, fontSize = 9.sp)
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(sub, style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
    }
}

data class EduCardModel(
    val id: String,
    val title: String,
    val duration: String,
    val views: String,
    val tagText: String,
    val tagColor: Color,
    val imageRes: Int
)
