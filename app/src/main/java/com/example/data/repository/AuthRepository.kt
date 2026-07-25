package com.example.data.repository

import com.example.data.local.UserPreferences
import com.example.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AuthRepository(private val prefs: UserPreferences) {

    suspend fun login(emailOrPhone: String, pass: String): Result<User> = withContext(Dispatchers.IO) {
        delay(1000) // Simulate network call to POST /api/auth/login
        if (emailOrPhone.isNotBlank() && pass.length >= 6) {
            prefs.isLoggedIn = true
            prefs.authToken = "jwt_token_faslaw_mock_${System.currentTimeMillis()}"
            if (emailOrPhone.contains("@")) {
                prefs.userEmail = emailOrPhone
            } else {
                prefs.userPhone = emailOrPhone
            }
            Result.success(
                User(
                    name = prefs.userName,
                    email = prefs.userEmail,
                    phone = prefs.userPhone
                )
            )
        } else {
            Result.failure(Exception("Email/No HP atau Password tidak valid"))
        }
    }

    suspend fun register(name: String, email: String, phone: String, pass: String): Result<User> = withContext(Dispatchers.IO) {
        delay(1200) // Simulate network call to POST /api/auth/register
        if (name.isNotBlank() && email.contains("@") && pass.length >= 8) {
            prefs.isLoggedIn = true
            prefs.userName = name
            prefs.userEmail = email
            prefs.userPhone = phone
            prefs.authToken = "jwt_token_faslaw_mock_${System.currentTimeMillis()}"
            Result.success(User(name = name, email = email, phone = phone))
        } else {
            Result.failure(Exception("Mohon lengkapi seluruh data dengan benar. Password min. 8 karakter."))
        }
    }

    suspend fun sendResetPasswordLink(email: String): Result<String> = withContext(Dispatchers.IO) {
        delay(800) // Simulate network call to POST /api/auth/forgot-password
        if (email.contains("@")) {
            Result.success("Tautan instruksi reset password telah dikirimkan ke $email. Silakan periksa kotak masuk/spam Anda.")
        } else {
            Result.failure(Exception("Format email tidak valid"))
        }
    }

    fun getCurrentUser(): User? {
        return if (prefs.isLoggedIn) {
            User(
                name = prefs.userName,
                email = prefs.userEmail,
                phone = prefs.userPhone
            )
        } else null
    }

    fun logout() {
        prefs.logout()
    }
}
