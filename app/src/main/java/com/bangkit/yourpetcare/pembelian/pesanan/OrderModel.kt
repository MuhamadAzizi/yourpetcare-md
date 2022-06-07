package com.bangkit.yourpetcare.pembelian.pesanan

import android.os.Parcelable
import com.bangkit.yourpetcare.pembelian.keranjang.KeranjangModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModel (
    var date: String? = null,
    var img : String? = null,
    var orderId : String? = null,
    var id_produk : List<KeranjangModel>? = null,
    var totalPrice : Long? = null,
    var userId : String? = null,
    var username : String? = null,
    var paymentProof : String? = null,
    var status : String? = "Belum Bayar"
):Parcelable