package com.bangkit.yourpetcare.konsultasi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dokter(
    val email: String,
    val username: String,
    val nama: String,
    val nomor: String,
    val riwayatPendidikan: String,
    val tempatPraktik: String,
    val uid: String,
    val spesialis: String,
    val image: String = "motorcycle.png"
) : Parcelable
