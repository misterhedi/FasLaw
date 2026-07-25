package com.example.presentation.navigation

sealed class NavRoutes(val route: String) {
    object Onboarding : NavRoutes("onboarding")
    object Login : NavRoutes("login")
    object Register : NavRoutes("register")
    object ForgotPassword : NavRoutes("forgot_password")

    object Home : NavRoutes("home")
    object AiChat : NavRoutes("ai_chat")
    object ExpertList : NavRoutes("expert_list")
    object ExpertChat : NavRoutes("expert_chat/{expertId}") {
        fun createRoute(expertId: String) = "expert_chat/$expertId"
    }
    object VideoCall : NavRoutes("video_call/{expertId}") {
        fun createRoute(expertId: String) = "video_call/$expertId"
    }
    object DocumentAnalysis : NavRoutes("document_analysis")
    object LbhLocator : NavRoutes("lbh_locator")
    object KnowledgeBase : NavRoutes("knowledge_base")
    object ArticleDetail : NavRoutes("article_detail/{articleId}") {
        fun createRoute(articleId: String) = "article_detail/$articleId"
    }
    object Profile : NavRoutes("profile")
    object ProBonoRegistration : NavRoutes("pro_bono_registration")
    object PrivacyPolicy : NavRoutes("privacy_policy")
    object HelpCenter : NavRoutes("help_center")
    object Settings : NavRoutes("settings")
    object DisputeMediation : NavRoutes("dispute_mediation")
}
