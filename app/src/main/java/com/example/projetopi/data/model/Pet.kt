package com.example.projetopi.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pet (
    val id: Int,
    val adopted: Status = Status.NOT_ADOPTED
): Parcelable