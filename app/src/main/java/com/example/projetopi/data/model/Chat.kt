package com.example.projetopi.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chat(
    val id: Int,
    val name: String
): Parcelable
