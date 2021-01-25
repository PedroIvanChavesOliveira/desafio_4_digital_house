package com.example.desafio_4.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game (
    val id: Int,
    val photo: String?,
    val description: String?,
    val release: String?,
    val title: String?
): Parcelable