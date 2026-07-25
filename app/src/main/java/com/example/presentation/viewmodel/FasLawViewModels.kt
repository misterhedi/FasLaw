package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferences
import com.example.data.repository.ArticleRepository
import com.example.data.repository.AuthRepository
import com.example.data.repository.ChatRepository
import com.example.data.repository.DocumentRepository
import com.example.data.repository.ExpertRepository
import com.example.data.repository.LbhRepository
import com.example.domain.model.Article
import com.example.domain.model.ChatMessage
import com.example.domain.model.ChatSender
import com.example.domain.model.DocumentAnalysisResult
import com.example.domain.model.Expert
import com.example.domain.model.LawPasal
import com.example.domain.model.LbhLocation
import com.example.domain.model.ProBonoApplication
import com.example.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// --- Auth ViewModel ---
class AuthViewModel(
    private val authRepo: AuthRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    val isLoggedIn = MutableStateFlow(userPrefs.isLoggedIn)
    val isOnboardingDone = MutableStateFlow(userPrefs.isOnboardingDone)

    val currentUser = MutableStateFlow<User?>(authRepo.getCurrentUser())

    val loginState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val registerState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val resetState = MutableStateFlow<UiState<String>>(UiState.Idle)

    fun markOnboardingDone() {
        userPrefs.isOnboardingDone = true
        isOnboardingDone.value = true
    }

    fun login(emailOrPhone: String, pass: String) {
        viewModelScope.launch {
            loginState.value = UiState.Loading
            val res = authRepo.login(emailOrPhone, pass)
            if (res.isSuccess) {
                val u = res.getOrNull()
                currentUser.value = u
                isLoggedIn.value = true
                loginState.value = UiState.Success(u!!)
            } else {
                loginState.value = UiState.Error(res.exceptionOrNull()?.message ?: "Gagal login")
            }
        }
    }

    fun register(name: String, email: String, phone: String, pass: String) {
        viewModelScope.launch {
            registerState.value = UiState.Loading
            val res = authRepo.register(name, email, phone, pass)
            if (res.isSuccess) {
                val u = res.getOrNull()
                currentUser.value = u
                isLoggedIn.value = true
                registerState.value = UiState.Success(u!!)
            } else {
                registerState.value = UiState.Error(res.exceptionOrNull()?.message ?: "Gagal daftar")
            }
        }
    }

    fun sendResetPasswordLink(email: String) {
        viewModelScope.launch {
            resetState.value = UiState.Loading
            val res = authRepo.sendResetPasswordLink(email)
            if (res.isSuccess) {
                resetState.value = UiState.Success(res.getOrNull()!!)
            } else {
                resetState.value = UiState.Error(res.exceptionOrNull()?.message ?: "Gagal mengirim tautan")
            }
        }
    }

    fun updateProfile(name: String, email: String, phone: String) {
        userPrefs.userName = name
        userPrefs.userEmail = email
        userPrefs.userPhone = phone
        currentUser.value = User(name, email, phone)
    }

    fun logout() {
        authRepo.logout()
        isLoggedIn.value = false
        currentUser.value = null
        loginState.value = UiState.Idle
        registerState.value = UiState.Idle
    }
}

// --- AI Chat ViewModel ---
class AiChatViewModel(private val chatRepo: ChatRepository) : ViewModel() {

    val currentSessionId = MutableStateFlow("session_default")
    val isTyping = MutableStateFlow(false)

    val messages: StateFlow<List<ChatMessage>> = chatRepo.getMessages("session_default")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            isTyping.value = true
            chatRepo.sendMessageToAi(currentSessionId.value, text)
            isTyping.value = false
        }
    }

    fun startNewChat() {
        val newSessionId = "session_${System.currentTimeMillis()}"
        currentSessionId.value = newSessionId
    }
}

// --- Expert Consultation ViewModel ---
class ExpertViewModel(private val expertRepo: ExpertRepository) : ViewModel() {

    val experts: StateFlow<List<Expert>> = MutableStateFlow(expertRepo.getExperts())

    private val _humanMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "exp_1" to listOf(
                ChatMessage(id = 1, sessionId = "exp_1", sender = ChatSender.EXPERT, text = "Halo! Saya Adv. Hendra Wijaya, S.H., M.H. Ada kasus hukum atau pertanyaan seputar pidana/cyber law yang ingin didiskusikan hari ini?")
            )
        )
    )
    val humanMessages: StateFlow<Map<String, List<ChatMessage>>> = _humanMessages.asStateFlow()

    fun sendExpertMessage(expertId: String, text: String) {
        if (text.isBlank()) return
        val currentList = _humanMessages.value[expertId].orEmpty().toMutableList()
        val userMsg = ChatMessage(id = System.currentTimeMillis(), sessionId = expertId, sender = ChatSender.USER, text = text)
        currentList.add(userMsg)

        // Simulate Expert response
        val replyMsg = ChatMessage(
            id = System.currentTimeMillis() + 1,
            sessionId = expertId,
            sender = ChatSender.EXPERT,
            text = "Terima kasih atas informasi awal mengenai hal tersebut. Saya telah mencatat poin utama perkaranya. Apakah Anda memiliki dokumen pendorong seperti surat perjanjian atau tangkapan layar transaksi?"
        )
        currentList.add(replyMsg)

        _humanMessages.value = _humanMessages.value.toMutableMap().apply {
            put(expertId, currentList)
        }
    }
}

// --- Document ViewModel ---
class DocumentViewModel(private val docRepo: DocumentRepository) : ViewModel() {

    val isAnalyzing = MutableStateFlow(false)
    val currentAnalysis = MutableStateFlow<DocumentAnalysisResult?>(null)

    val history: StateFlow<List<DocumentAnalysisResult>> = docRepo.getDocumentHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun analyzeDocument(fileName: String) {
        viewModelScope.launch {
            isAnalyzing.value = true
            currentAnalysis.value = null
            val res = docRepo.analyzeDocument(fileName)
            if (res.isSuccess) {
                currentAnalysis.value = res.getOrNull()
            }
            isAnalyzing.value = false
        }
    }
}

// --- LBH Locator ViewModel ---
class LbhViewModel(private val lbhRepo: LbhRepository) : ViewModel() {
    private val rawLbhList = lbhRepo.getNearbyLbh()

    val searchQuery = MutableStateFlow("")
    val userLocation = MutableStateFlow<Pair<Double, Double>?>(null) // (Lat, Lng)
    val isLocationPermissionGranted = MutableStateFlow(false)

    private val _lbhList = MutableStateFlow(rawLbhList)
    val lbhList: StateFlow<List<LbhLocation>> = _lbhList.asStateFlow()

    val selectedLbh = MutableStateFlow<LbhLocation?>(rawLbhList.firstOrNull())

    fun updatePermissionState(granted: Boolean) {
        isLocationPermissionGranted.value = granted
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
        filterAndSortList()
    }

    fun updateUserLocation(lat: Double, lng: Double) {
        userLocation.value = Pair(lat, lng)
        filterAndSortList()
    }

    private fun filterAndSortList() {
        val query = searchQuery.value.trim()
        val userLoc = userLocation.value

        var updated = rawLbhList.map { lbh ->
            if (userLoc != null) {
                val dist = calculateDistanceKm(userLoc.first, userLoc.second, lbh.latitude, lbh.longitude)
                lbh.copy(distanceKm = dist)
            } else {
                lbh
            }
        }

        if (query.isNotEmpty()) {
            updated = updated.filter {
                it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
            }
        }

        if (userLoc != null) {
            updated = updated.sortedBy { it.distanceKm }
        }

        _lbhList.value = updated
        if (selectedLbh.value == null || updated.none { it.id == selectedLbh.value?.id }) {
            selectedLbh.value = updated.firstOrNull()
        }
    }

    private fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return Math.round(r * c * 10.0) / 10.0
    }
}

// --- Knowledge Base ViewModel ---
class KnowledgeViewModel(private val articleRepo: ArticleRepository) : ViewModel() {

    val selectedCategory = MutableStateFlow("Semua")
    val searchQuery = MutableStateFlow("")

    val articles = MutableStateFlow<List<Article>>(articleRepo.getArticles())
    val laws = MutableStateFlow<List<LawPasal>>(articleRepo.getLaws())

    fun updateCategory(cat: String) {
        selectedCategory.value = cat
        articles.value = articleRepo.getArticles(category = cat, searchQuery = searchQuery.value)
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
        articles.value = articleRepo.getArticles(category = selectedCategory.value, searchQuery = query)
        laws.value = articleRepo.getLaws(searchQuery = query)
    }

    fun getArticleById(id: String): Article? = articleRepo.getArticleById(id)
}

// --- Theme & Settings ViewModel ---
open class ThemeViewModel(private val userPrefs: UserPreferences) : ViewModel() {
    private val _isDarkMode = MutableStateFlow(userPrefs.isDarkMode)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isNotificationsEnabled = MutableStateFlow(userPrefs.isNotificationsEnabled)
    val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled.asStateFlow()

    private val _isConsultationAlertsEnabled = MutableStateFlow(userPrefs.isConsultationAlertsEnabled)
    val isConsultationAlertsEnabled: StateFlow<Boolean> = _isConsultationAlertsEnabled.asStateFlow()

    private val _isLegalNewsAlertsEnabled = MutableStateFlow(userPrefs.isLegalNewsAlertsEnabled)
    val isLegalNewsAlertsEnabled: StateFlow<Boolean> = _isLegalNewsAlertsEnabled.asStateFlow()

    private val _appLanguage = MutableStateFlow(userPrefs.appLanguage)
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    private val _isHighContrastEnabled = MutableStateFlow(userPrefs.isHighContrastEnabled)
    val isHighContrastEnabled: StateFlow<Boolean> = _isHighContrastEnabled.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        userPrefs.isDarkMode = enabled
        _isDarkMode.value = enabled
    }

    fun toggleDarkMode() {
        setDarkMode(!_isDarkMode.value)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        userPrefs.isNotificationsEnabled = enabled
        _isNotificationsEnabled.value = enabled
    }

    fun setConsultationAlertsEnabled(enabled: Boolean) {
        userPrefs.isConsultationAlertsEnabled = enabled
        _isConsultationAlertsEnabled.value = enabled
    }

    fun setLegalNewsAlertsEnabled(enabled: Boolean) {
        userPrefs.isLegalNewsAlertsEnabled = enabled
        _isLegalNewsAlertsEnabled.value = enabled
    }

    fun setAppLanguage(languageCode: String) {
        userPrefs.appLanguage = languageCode
        _appLanguage.value = languageCode
    }

    fun setHighContrastEnabled(enabled: Boolean) {
        userPrefs.isHighContrastEnabled = enabled
        _isHighContrastEnabled.value = enabled
    }
}

typealias SettingsViewModel = ThemeViewModel

// --- Pro Bono ViewModel ---
class ProBonoViewModel : ViewModel() {

    private val _applications = MutableStateFlow<List<ProBonoApplication>>(
        listOf(
            ProBonoApplication(
                ticketId = "PB-2026-8812",
                category = "Ketenagakerjaan (PHK)",
                summary = "Pendampingan pemutusan hubungan kerja tanpa sisa pesangon dari perusahaan.",
                urgency = "Penting",
                applicantName = "Budi Santoso",
                phone = "081234567890",
                email = "budi.santoso@email.com",
                city = "Jakarta Selatan",
                financialDeclaration = "Di Bawah Rp 3.000.000 / Bulan (Penghasilan Terkena PHK)",
                preferredContact = "WhatsApp & Telepon",
                status = "Verifikasi Tim LBH",
                assignedLbhName = "Posbakum LBH Jakarta"
            )
        )
    )
    val applications: StateFlow<List<ProBonoApplication>> = _applications.asStateFlow()

    val lastSubmittedApplication = MutableStateFlow<ProBonoApplication?>(null)

    fun submitApplication(
        category: String,
        summary: String,
        urgency: String,
        applicantName: String,
        phone: String,
        email: String,
        city: String,
        financialDeclaration: String,
        preferredContact: String
    ): ProBonoApplication {
        val randomNum = (1000..9999).random()
        val newTicket = ProBonoApplication(
            ticketId = "PB-2026-$randomNum",
            category = category,
            summary = summary,
            urgency = urgency,
            applicantName = applicantName,
            phone = phone,
            email = email,
            city = city,
            financialDeclaration = financialDeclaration,
            preferredContact = preferredContact,
            timestamp = System.currentTimeMillis(),
            status = "Pengajuan Berhasil Diterima",
            assignedLbhName = "Posbakum LBH $city"
        )
        _applications.value = listOf(newTicket) + _applications.value
        lastSubmittedApplication.value = newTicket
        return newTicket
    }
}

// --- Generic UiState Sealed Class ---
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// --- ViewModel Factory ---
class ViewModelFactory(
    private val authRepo: AuthRepository,
    private val chatRepo: ChatRepository,
    private val expertRepo: ExpertRepository,
    private val docRepo: DocumentRepository,
    private val lbhRepo: LbhRepository,
    private val articleRepo: ArticleRepository,
    private val userPrefs: UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(authRepo, userPrefs) as T
            modelClass.isAssignableFrom(AiChatViewModel::class.java) ->
                AiChatViewModel(chatRepo) as T
            modelClass.isAssignableFrom(ExpertViewModel::class.java) ->
                ExpertViewModel(expertRepo) as T
            modelClass.isAssignableFrom(DocumentViewModel::class.java) ->
                DocumentViewModel(docRepo) as T
            modelClass.isAssignableFrom(LbhViewModel::class.java) ->
                LbhViewModel(lbhRepo) as T
            modelClass.isAssignableFrom(KnowledgeViewModel::class.java) ->
                KnowledgeViewModel(articleRepo) as T
            modelClass.isAssignableFrom(ProBonoViewModel::class.java) ->
                ProBonoViewModel() as T
            modelClass.isAssignableFrom(ThemeViewModel::class.java) ->
                ThemeViewModel(userPrefs) as T
            modelClass.isAssignableFrom(MediationViewModel::class.java) ->
                MediationViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class $modelClass")
        }
    }
}
