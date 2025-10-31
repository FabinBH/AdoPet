package com.example.projetopi.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pet (
    val descricao: String? = null,
    val disponivel: Boolean = true,
    val dono: String? = null,
    val especie: String? = null,
    val fotoUrl: String? = null,
    val idade: Int? = null,
    val nome: String? = null,
    val raca: String? = null
): Parcelable