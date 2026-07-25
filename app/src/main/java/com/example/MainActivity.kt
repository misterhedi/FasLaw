package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferences
import com.example.data.repository.ArticleRepository
import com.example.data.repository.AuthRepository
import com.example.data.repository.ChatRepository
import com.example.data.repository.DocumentRepository
import com.example.data.repository.ExpertRepository
import com.example.data.repository.LbhRepository
import com.example.presentation.navigation.NavRoutes
import com.example.presentation.ui.auth.ForgotPasswordScreen
import com.example.presentation.ui.auth.LoginScreen
import com.example.presentation.ui.auth.RegisterScreen
import com.example.presentation.ui.chat.AiChatScreen
import com.example.presentation.ui.chat.ExpertChatScreen
import com.example.presentation.ui.chat.ExpertListScreen
import com.example.presentation.ui.chat.VideoCallScreen
import com.example.presentation.ui.document.DocumentAnalysisScreen
import com.example.presentation.ui.home.HomeScreen
import com.example.presentation.ui.knowledge.ArticleDetailScreen
import com.example.presentation.ui.knowledge.KnowledgeBaseScreen
import com.example.presentation.ui.lbh.LbhLocatorScreen
import com.example.presentation.ui.mediation.DisputeMediationScreen
import com.example.presentation.ui.onboarding.OnboardingScreen
import com.example.presentation.ui.probono.ProBonoRegistrationScreen
import com.example.presentation.ui.profile.HelpCenterScreen
import com.example.presentation.ui.profile.PrivacyPolicyScreen
import com.example.presentation.ui.profile.ProfileScreen
import com.example.presentation.ui.profile.SettingsScreen
import com.example.presentation.viewmodel.AiChatViewModel
import com.example.presentation.viewmodel.AuthViewModel
import com.example.presentation.viewmodel.DocumentViewModel
import com.example.presentation.viewmodel.ExpertViewModel
import com.example.presentation.viewmodel.KnowledgeViewModel
import com.example.presentation.viewmodel.LbhViewModel
import com.example.presentation.viewmodel.MediationViewModel
import com.example.presentation.viewmodel.ProBonoViewModel
import com.example.presentation.viewmodel.ThemeViewModel
import com.example.presentation.viewmodel.ViewModelFactory
import com.example.ui.theme.FasLawTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(applicationContext)
        val userPrefs = UserPreferences(applicationContext)

        val authRepo = AuthRepository(userPrefs)
        val chatRepo = ChatRepository(db.chatDao())
        val expertRepo = ExpertRepository()
        val docRepo = DocumentRepository(db.documentDao())
        val lbhRepo = LbhRepository()
        val articleRepo = ArticleRepository(db.bookmarkDao())

        val factory = ViewModelFactory(
            authRepo, chatRepo, expertRepo, docRepo, lbhRepo, articleRepo, userPrefs
        )

        setContent {
            val themeViewModel: ThemeViewModel = viewModel(factory = factory)
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            FasLawTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FasLawAppNavigation(factory, userPrefs, themeViewModel)
                }
            }
        }
    }
}

@Composable
fun FasLawAppNavigation(
    factory: ViewModelFactory,
    userPrefs: UserPreferences,
    themeViewModel: ThemeViewModel
) {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val chatViewModel: AiChatViewModel = viewModel(factory = factory)
    val expertViewModel: ExpertViewModel = viewModel(factory = factory)
    val docViewModel: DocumentViewModel = viewModel(factory = factory)
    val lbhViewModel: LbhViewModel = viewModel(factory = factory)
    val knowledgeViewModel: KnowledgeViewModel = viewModel(factory = factory)
    val proBonoViewModel: ProBonoViewModel = viewModel(factory = factory)
    val mediationViewModel: MediationViewModel = viewModel(factory = factory)

    val startDestination = when {
        !userPrefs.isOnboardingDone -> NavRoutes.Onboarding.route
        !userPrefs.isLoggedIn -> NavRoutes.Login.route
        else -> NavRoutes.Home.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Onboarding
        composable(NavRoutes.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    authViewModel.markOnboardingDone()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Login
        composable(NavRoutes.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(NavRoutes.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(NavRoutes.ForgotPassword.route)
                }
            )
        }

        // Register
        composable(NavRoutes.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Forgot Password
        composable(NavRoutes.ForgotPassword.route) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Home (Dashboard)
        composable(NavRoutes.Home.route) {
            HomeScreen(
                authViewModel = authViewModel,
                knowledgeViewModel = knowledgeViewModel,
                onNavigateToAiChat = { navController.navigate(NavRoutes.AiChat.route) },
                onNavigateToExpertList = { navController.navigate(NavRoutes.ExpertList.route) },
                onNavigateToDocumentAnalysis = { navController.navigate(NavRoutes.DocumentAnalysis.route) },
                onNavigateToLbhLocator = { navController.navigate(NavRoutes.LbhLocator.route) },
                onNavigateToKnowledgeBase = { navController.navigate(NavRoutes.KnowledgeBase.route) },
                onNavigateToArticleDetail = { id -> navController.navigate(NavRoutes.ArticleDetail.createRoute(id)) },
                onNavigateToProfile = { navController.navigate(NavRoutes.Profile.route) },
                onNavigateToQuickPrompt = { prompt ->
                    chatViewModel.sendMessage(prompt)
                    navController.navigate(NavRoutes.AiChat.route)
                },
                onNavigateToProBono = { navController.navigate(NavRoutes.ProBonoRegistration.route) }
            )
        }

        // AI Chat Screen
        composable(NavRoutes.AiChat.route) {
            AiChatScreen(
                viewModel = chatViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Expert List Screen
        composable(NavRoutes.ExpertList.route) {
            ExpertListScreen(
                viewModel = expertViewModel,
                onNavigateToChat = { id -> navController.navigate(NavRoutes.ExpertChat.createRoute(id)) },
                onNavigateToVideoCall = { id -> navController.navigate(NavRoutes.VideoCall.createRoute(id)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Expert Chat Screen
        composable(
            route = NavRoutes.ExpertChat.route,
            arguments = listOf(navArgument("expertId") { type = NavType.StringType })
        ) { backStackEntry ->
            val expertId = backStackEntry.arguments?.getString("expertId") ?: "exp_1"
            ExpertChatScreen(
                expertId = expertId,
                viewModel = expertViewModel,
                onNavigateToVideoCall = { id -> navController.navigate(NavRoutes.VideoCall.createRoute(id)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Video Call Screen
        composable(
            route = NavRoutes.VideoCall.route,
            arguments = listOf(navArgument("expertId") { type = NavType.StringType })
        ) { backStackEntry ->
            val expertId = backStackEntry.arguments?.getString("expertId") ?: "exp_1"
            VideoCallScreen(
                expertId = expertId,
                viewModel = expertViewModel,
                onEndCall = { navController.popBackStack() }
            )
        }

        // Document Analysis Screen
        composable(NavRoutes.DocumentAnalysis.route) {
            DocumentAnalysisScreen(
                viewModel = docViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // LBH Locator Screen
        composable(NavRoutes.LbhLocator.route) {
            LbhLocatorScreen(
                viewModel = lbhViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProBono = { navController.navigate(NavRoutes.ProBonoRegistration.route) }
            )
        }

        // Pro Bono Registration Screen
        composable(NavRoutes.ProBonoRegistration.route) {
            ProBonoRegistrationScreen(
                proBonoViewModel = proBonoViewModel,
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAiChat = { navController.navigate(NavRoutes.AiChat.route) }
            )
        }

        // Knowledge Base Screen
        composable(NavRoutes.KnowledgeBase.route) {
            KnowledgeBaseScreen(
                viewModel = knowledgeViewModel,
                onNavigateToArticleDetail = { id -> navController.navigate(NavRoutes.ArticleDetail.createRoute(id)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Article Detail Screen
        composable(
            route = NavRoutes.ArticleDetail.route,
            arguments = listOf(navArgument("articleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId") ?: "art_1"
            ArticleDetailScreen(
                articleId = articleId,
                viewModel = knowledgeViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Profile Screen
        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                themeViewModel = themeViewModel,
                onLogout = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPrivacyPolicy = { navController.navigate(NavRoutes.PrivacyPolicy.route) },
                onNavigateToHelpCenter = { navController.navigate(NavRoutes.HelpCenter.route) },
                onNavigateToSettings = { navController.navigate(NavRoutes.Settings.route) }
            )
        }

        // Settings Screen
        composable(NavRoutes.Settings.route) {
            SettingsScreen(
                viewModel = themeViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Privacy Policy Screen
        composable(NavRoutes.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Help Center / FAQ Screen
        composable(NavRoutes.HelpCenter.route) {
            HelpCenterScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
