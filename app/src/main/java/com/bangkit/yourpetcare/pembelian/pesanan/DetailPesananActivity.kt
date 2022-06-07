package com.bangkit.yourpetcare.pembelian.pesanan

import android.icu.number.NumberFormatter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityDetailPesananBinding
import com.bangkit.yourpetcare.pembelian.keranjang.KeranjangAdapter
import com.bangkit.yourpetcare.pembelian.keranjang.KeranjangModel
import com.google.firebase.auth.FirebaseAuth
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailPesananActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_ORDER = "pemesanan"
    }

    private var binding : ActivityDetailPesananBinding? = null
    private var model : OrderModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPesananBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val formater : NumberFormat = DecimalFormat("#,###")
        model = intent.getParcelableExtra(EXTRA_ORDER)
        binding?.tvHarusDibayar?.text = "Rp. ${formater.format(model?.totalPrice)}"
        binding?.tvIsiSubtotal?.text = "Rp. ${formater.format(model?.totalPrice)}"

        binding?.imageButton?.setOnClickListener {
            onBackPressed()
        }

        initOrderList()
    }

    private fun initOrderList() {
        binding?.rvProdukPesanan?.layoutManager = (LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL,false))

        val adapter = KeranjangAdapter(null, null, "order")
        binding?.rvProdukPesanan?.adapter = adapter
        adapter.setData(model?.id_produk as ArrayList<KeranjangModel>)
    }
}