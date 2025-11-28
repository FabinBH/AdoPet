package com.example.projetopi.data.model

data class Ong (
    val id: Int,
    val nome: String,
    val fotoUrl: String? = null,
    val cnpj: String? = null,
    val email: String? = null,
    val telefone: String? = null
)
