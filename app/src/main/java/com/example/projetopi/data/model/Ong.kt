package com.example.projetopi.data.model

data class Ong (
    val id: Int,
    val nome: String,
    val fotoUrl: String? = null,
    val cnpj: String?,
    val email: String?,
    val telefone: String?
)
