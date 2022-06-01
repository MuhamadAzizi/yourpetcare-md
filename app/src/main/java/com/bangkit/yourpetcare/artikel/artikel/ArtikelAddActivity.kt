package com.bangkit.yourpetcare.artikel.artikel

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityArtikelAddBinding
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

class ArtikelAddActivity : AppCompatActivity() {

    private var binding : ActivityArtikelAddBinding?= null
    private var dp : String? = null
    private val request_gallery = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtikelAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.add?.setOnClickListener{
            validation()
        }

        binding?.imgAdd?.setOnClickListener{
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(request_gallery)
        }
    }

    private fun validation() {
        val judul = binding?.judulArtikelAdd?.text.toString().trim()
        val deskripsi = binding?.dekripsiArikelAdd?.text.toString().trim()

        when{
            judul.isEmpty() -> {
                Toast.makeText(this, "Judul jangan kosong", Toast.LENGTH_SHORT).show()
            }
            deskripsi.isEmpty()->{
                Toast.makeText(this, "Deskripsi jangan kosong", Toast.LENGTH_SHORT).show()
            }
            dp == null -> {
                Toast.makeText(this, "Gambar jangan kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val uid = System.currentTimeMillis().toString()
                val data = mapOf(
                    "judul_artikel" to judul,
                    "deskripsi_artikel" to deskripsi,
                    "image_artikel" to dp
                )

                FirebaseFirestore
                    .getInstance()
                    .collection("artikel")
                    .document(uid)
                    .set(data)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            successDialog()
                        }else{
                            failurDialog()
                        }
                    }
            }
        }
    }

    private fun failurDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Menambahkan Data")
            .setMessage("Anda gagal menambahkan data hewan, cek koneksi internet anda")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("oke"){
                    dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun successDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Menambahkan Data")
            .setMessage("Selamat anda berhasil manambahkan artikel")
            .setIcon(R.drawable.ic_baseline_check_24)
            .setPositiveButton("oke"){
                    dialog, _ ->
                dialog.dismiss()
                onBackPressed()
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            if(requestCode == request_gallery){
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
        val imageFileName = "artikel/image_" + System.currentTimeMillis() + " .png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        progresDialog.dismiss()
                        dp = uri.toString()
                        Glide.with(this).load(dp).into(binding!!.imgAddArtikel)
                    }
                    .addOnFailureListener {
                        e:Exception->progresDialog.dismiss()
                        Toast.makeText(this, "Gagal unggah gambar", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener{e:Exception ->
                progresDialog.dismiss()
                Toast.makeText(this, "Gagal unggah gambar", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}