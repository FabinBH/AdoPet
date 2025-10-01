package com.example.projetopi.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pet (
    val id: String,
    val description: String
): Parcelable