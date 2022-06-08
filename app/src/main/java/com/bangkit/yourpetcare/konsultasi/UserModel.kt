package com.bangkit.yourpetcare.konsultasi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel (
     var username : String? = null,
     var email : String? = null,
     var role : String? = null,
     var image : String? = null,
     var spesialis : String? = null,
     var no_str : String? = null,
     var riwayat_pendidikan : String? = null,
     var tempat_praktik : String? = null,
     var uid : String? = null
):Parcelable