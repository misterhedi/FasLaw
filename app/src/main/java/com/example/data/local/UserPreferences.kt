package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("faslaw_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_ONBOARDING_DONE = "onboarding_done"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_CONSULTATION_ALERTS = "consultation_alerts"
        private const val KEY_LEGAL_NEWS_ALERTS = "legal_news_alerts"
        private const val KEY_APP_LANGUAGE = "app_language"
        private const val KEY_HIGH_CONTRAST = "high_contrast"
    }

    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_IS_DARK_MODE, true)
        set(value) = prefs.edit().putBoolean(KEY_IS_DARK_MODE, value).apply()

    var isNotificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var isConsultationAlertsEnabled: Boolean
        get() = prefs.getBoolean(KEY_CONSULTATION_ALERTS, true)
        set(value) = prefs.edit().putBoolean(KEY_CONSULTATION_ALERTS, value).apply()

    var isLegalNewsAlertsEnabled: Boolean
        get() = prefs.getBoolean(KEY_LEGAL_NEWS_ALERTS, true)
        set(value) = prefs.edit().putBoolean(KEY_LEGAL_NEWS_ALERTS, value).apply()

    var appLanguage: String
        get() = prefs.getString(KEY_APP_LANGUAGE, "id") ?: "id"
        set(value) = prefs.edit().putString(KEY_APP_LANGUAGE, value).apply()

    var isHighContrastEnabled: Boolean
        get() = prefs.getBoolean(KEY_HIGH_CONTRAST, false)
        set(value) = prefs.edit().putBoolean(KEY_HIGH_CONTRAST, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var isOnboardingDone: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_DONE, value).apply()

    var userName: String
        get() = prefs.getString(KEY_USER_NAME, "Budi Santoso") ?: "Budi Santoso"
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    var userEmail: String
        get() = prefs.getString(KEY_USER_EMAIL, "budi.santoso@email.com") ?: "budi.santoso@email.com"
        set(value) = prefs.edit().putString(KEY_USER_EMAIL, value).apply()

    var userPhone: String
        get() = prefs.getString(KEY_USER_PHONE, "081234567890") ?: "081234567890"
        set(value) = prefs.edit().putString(KEY_USER_PHONE, value).apply()

    var authToken: String?
        get() = prefs.getString(KEY_AUTH_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_AUTH_TOKEN, value).apply()

    fun logout() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .remove(KEY_AUTH_TOKEN)
            .apply()
    }
}
