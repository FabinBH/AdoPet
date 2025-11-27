package com.example.projetopi.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ong (
    val id: Int,
    val nome: String,
    val fotoUrl: String? = null
): Parcelable
