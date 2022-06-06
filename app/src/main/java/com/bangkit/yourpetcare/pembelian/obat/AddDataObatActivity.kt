package com.bangkit.yourpetcare.pembelian.obat

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityAddDataProdukBinding
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

class AddDataObatActivity : AppCompatActivity() {

    private var binding: ActivityAddDataProdukBinding? = null
    private var dp: String? = null
    private val request_gallery = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataProdukBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.add?.setOnClickListener {
            validation()
        }
        binding?.imgAdd?.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(request_gallery)
        }
    }

    private fun validation() {
        val namaProduk = binding?.namaProdukAdd?.text.toString().trim()
        val deskripsiProduk = binding?.dekripsiProdukAdd?.text.toString().trim()
        val hargaProduk = binding?.hargaProdukAdd?.text.toString().trim()

        when {
            namaProduk.isEmpty() -> {
                Toast.makeText(this, "Nama produk jangan kosong", Toast.LENGTH_SHORT).show()
            }
            deskripsiProduk.isEmpty() -> {
                Toast.makeText(this, "Deskripsi produk jangan kosong", Toast.LENGTH_SHORT).show()
            }
            hargaProduk.isEmpty() -> {
                Toast.makeText(this, "Harga produk jangan kosong", Toast.LENGTH_SHORT).show()
            }
            dp == null -> {
                Toast.makeText(this, "Gambar jangan kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val uid = System.currentTimeMillis().toString()
                val data = mapOf(
                    "category" to "Obat",
                    "nama_produk" to namaProduk,
                    "deskripsi_produk" to deskripsiProduk,
                    "harga_produk" to hargaProduk.toLong(),
                    "image_produk" to dp,
                    "produk_id" to uid
                )

                FirebaseFirestore
                    .getInstance()
                    .collection("produk")
                    .document(uid)
                    .set(data)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            successDialog()
                        } else {
                            failurDialog()
                        }
                    }

            }

        }

    }

    private fun failurDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Menambahkan Data")
            .setMessage("Anda gagal menambahkan data produk, cek koneksi internet anda")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("oke") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun successDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Menambahkan Data")
            .setMessage("Selamat anda berhasil manambahkan data produk")
            .setIcon(R.drawable.ic_baseline_check_24)
            .setPositiveButton("oke") { dialog, _ ->
                dialog.dismiss()
                onBackPressed()
            }.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == request_gallery) {
                uploadDp(data?.data)
            }
        }
    }

    private fun uploadDp(data: Uri?) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val progresDialog = ProgressDialog(this)
        progresDialog.setMessage("Mohon ditunggu hingga selesai")
        progresDialog.setCanceledOnTouchOutside(false)
        progresDialog.show()
        val imageFileName = "produk/image_" + System.currentTimeMillis() + " .png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        progresDialog.dismiss()
                        dp = uri.toString()
                        Glide
                            .with(this)
                            .load(dp)
                            .into(binding!!.imgAddProduk)
                    }
                    .addOnFailureListener { e: Exception ->
                        progresDialog.dismiss()
                        Toast.makeText(this, "Gagal unggah gambar", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e: Exception ->
                progresDialog.dismiss()
                Toast.makeText(this, "Gagal unggah gambar", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}