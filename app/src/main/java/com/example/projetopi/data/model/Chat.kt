package com.example.projetopi.data.model

data class Chat(
    val id: Int,
    val nome: String,
    val email: String?,
    val fotoBase64: String? = null
)
