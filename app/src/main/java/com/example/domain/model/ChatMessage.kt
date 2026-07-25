package com.example.domain.model

data class ChatMessage(
    val id: Long = 0,
    val sessionId: String,
    val sender: ChatSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class ChatSender {
    USER, AI, EXPERT
}
