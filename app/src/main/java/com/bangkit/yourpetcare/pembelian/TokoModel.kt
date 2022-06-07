package com.bangkit.yourpetcare.pembelian

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokoModel (
    var category : String? = null,
    var namaProduk : String? = null,
    var deskripsiProduk : String? = null,
    var produk_id : String? = null,
    var hargaProduk : Long? = null,
    var imgProduk : String? = null,
    var uid : String? = null,
):Parcelable