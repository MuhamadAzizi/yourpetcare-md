package com.bangkit.yourpetcare.pembelian.keranjang

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeranjangModel (
    var cartId : String? = null,
    var userId : String? = null,
    var name : String? = null,
    var qty : Long? = null,
    var price : Long? = null,
    var deskripsi : String? = null,
    var image : String? = null,
):Parcelable