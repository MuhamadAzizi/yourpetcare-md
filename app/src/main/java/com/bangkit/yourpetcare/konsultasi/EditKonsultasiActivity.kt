package com.bangkit.yourpetcare.konsultasi

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityEditBinding
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

class EditKonsultasiActivity : AppCompatActivity() {

    private var binding: ActivityEditBinding? = null
    private var model: UserModel? = null
    private var dp: String? = null
    private var REQUEST_GALLERY = 1001

    companion object {
        const val EXTRA_EDIT = "editdokter"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        model = intent.getParcelableExtra(EXTRA_EDIT)

        binding?.namaDokterEdit?.setText(model?.username)
        binding?.spesialisDokterEdit?.setText(model?.spesialis)
        binding?.nomorstrEdit?.setText(model?.no_str)
        binding?.riwayatpendidikanEdit?.setText(model?.riwayat_pendidikan)
        binding?.tempatpraktikEdit?.setText(model?.tempat_praktik)


        binding?.imgAddFoto?.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_GALLERY)
        }

        binding?.perbarui?.setOnClickListener {
            perbaruiDokter()
        }
    }

    private fun perbaruiDokter() {
        val namaDokter = binding?.namaDokterEdit?.text.toString().trim()
        val spesialisDokter = binding?.spesialisDokterEdit?.text.toString().trim()
        val riwayatPendidikanDokter = binding?.riwayatpendidikanEdit?.text.toString().trim()
        val tempatPraktikDokter = binding?.tempatpraktikEdit?.text.toString().trim()
        val noStr = binding?.nomorstrEdit?.text.toString().trim()

        when {
            namaDokter.isEmpty() -> {
                Toast.makeText(this, "Nama dokter jangan kosong", Toast.LENGTH_SHORT).show()
            }
            spesialisDokter.isEmpty() -> {
                Toast.makeText(this, "Spesialis dokter jangan kosong", Toast.LENGTH_SHORT).show()
            }
            riwayatPendidikanDokter.isEmpty() -> {
                Toast.makeText(this, "Riwayat dokter jangan kosong", Toast.LENGTH_SHORT).show()
            }
            tempatPraktikDokter.isEmpty() -> {
                Toast.makeText(this, "Tempat praktik jangan kosong", Toast.LENGTH_SHORT).show()
            }
            noStr.isEmpty() -> {
                Toast.makeText(this, "No STR jangan kosong", Toast.LENGTH_SHORT).show()
            }
            dp == null -> {
                Toast.makeText(this, "Gambar jangan kosong", Toast.LENGTH_SHORT).show()
            }

            else -> {
                binding?.progressbarEdit?.visibility = View.VISIBLE
                val data = mapOf(
                    "username" to namaDokter,
                    "spesialis" to spesialisDokter,
                    "riwayat_pendidikan" to riwayatPendidikanDokter,
                    "tempat_praktik" to tempatPraktikDokter,
                    "no_str" to noStr,
                    "image" to dp,
                )
                model?.uid?.let { it->
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(it)
                        .update(data)
                        .addOnCompleteListener {data->
                            if (data.isSuccessful) {
                                binding?.progressbarEdit?.visibility = View.GONE
                                successDialog()
                            } else {
                                binding?.progressbarEdit?.visibility = View.GONE
                                failurDialog()
                            }
                        }
                }
            }
        }

    }

    private fun failurDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Memperbarui Data")
            .setMessage("Anda gagal memperbarui data diri, cek koneksi internet anda")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("oke") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun successDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Memperbarui Data")
            .setMessage("Selamat anda berhasil memperbarui data diri anda")
            .setIcon(R.drawable.ic_baseline_check_24)
            .setPositiveButton("oke") { dialog, _ ->
                dialog.dismiss()
                binding?.progressbarEdit?.visibility = View.VISIBLE
                onBackPressed()
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
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
        val imageFileName = "user/image_" + System.currentTimeMillis() + " .png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        progresDialog.dismiss()
                        dp = uri.toString()
                        Glide
                            .with(this)
                            .load(dp)
                            .into(binding!!.imgFoto)
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