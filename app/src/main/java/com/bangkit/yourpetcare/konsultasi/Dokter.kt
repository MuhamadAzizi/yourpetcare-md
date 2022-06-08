package com.bangkit.yourpetcare.konsultasi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dokter(
    val email: String? = null,
    val username: String? = null,
    val nama: String? = null,
    val nomor: String? = null,
    val riwayatPendidikan: String? = null,
    val tempatPraktik: String? = null,
    val uid: String? = null,
    val spesialis: String? = null,
    val image: String? = null
) : Parcelable
