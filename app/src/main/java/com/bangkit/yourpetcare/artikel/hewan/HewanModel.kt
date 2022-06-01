package com.bangkit.yourpetcare.artikel.hewan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HewanModel(
    var nama_hewan : String? = null,
    var deskripsi_hewan : String? = null,
    var image_hewan : String? = null,
    var uid : String? = null
) : Parcelable