package com.bangkit.yourpetcare.artikel.artikel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArtikelModel(
    var judul_artikel : String? = null,
    var deskripsi : String? = null,
    var image : String? = null,
    var uid : String? = null
) : Parcelable