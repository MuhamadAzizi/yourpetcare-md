package com.bangkit.yourpetcare.pembelian

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.artikel.artikel.ArtikelModel
import com.bangkit.yourpetcare.databinding.ActivityDetailPembelianBinding
import com.bangkit.yourpetcare.pembelian.keranjang.KeranjangActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailPembelianActivity : AppCompatActivity() {

    private var binding : ActivityDetailPembelianBinding? = null
    private var model : TokoModel? = null
    companion object{
        const val EXTRA_PEMBELIAN = "artikel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPembelianBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.imageButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.tambahKeranjangDetailPembelian?.setOnClickListener {
            showPopupQty()
        }

        model = intent.getParcelableExtra(EXTRA_PEMBELIAN)
        val formatter: NumberFormat = DecimalFormat("#,###")

        Glide.with(this)
            .load(model?.imgProduk)
            .into(binding!!.imgPembelian)
        binding?.namaProdukDetail?.text = model?.namaProduk
        binding?.tvDeskripsiDetailPembelian?.text = model?.deskripsiProduk
        binding?.hargaProdukDetail?.text = "Rp. ${formatter.format(model?.hargaProduk)}"

    }

    private fun showPopupQty() {
        val btnSubmit : Button
        val btnCancel : Button
        val etQty : EditText
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_popup)
        dialog.setCanceledOnTouchOutside(false)
        btnSubmit = dialog.findViewById(R.id.submit)
        btnCancel = dialog.findViewById(R.id.cancel)
        etQty = dialog.findViewById(R.id.qty)

        btnSubmit.setOnClickListener {
            val qty = etQty.text.toString().trim()

            if(qty.isEmpty()){
                Toast.makeText(this, "Quantity jangan kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Mohon ditunggu terlebih dahulu ... ")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            val cartId = System.currentTimeMillis().toString()
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val price = model?.hargaProduk?.times(qty.toLong())

            val cart = mapOf(
                "cartId" to cartId,
                "userId" to uid,
                "produk_id" to model?.idProduk,
                "nama_produk" to model?.namaProduk,
                "qty" to qty.toLong(),
                "description_produk" to model?.deskripsiProduk,
                "img" to model?.imgProduk,
                "price" to price
            )

            FirebaseFirestore
                .getInstance()
                .collection("cart")
                .document(cartId)
                .set(cart)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        progressDialog.dismiss()
                        dialog.dismiss()
                        Toast.makeText(this, "Berhasil menambahkan produk ${model?.namaProduk}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, KeranjangActivity::class.java))
                    }else{
                        progressDialog.dismiss()
                        dialog.dismiss()
                        Toast.makeText(this, "Gagal menambahkan produk", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


    }
}