package com.bangkit.yourpetcare.konsultasi.detail_dokter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityDetailDokterBinding
import com.bangkit.yourpetcare.konsultasi.Dokter
import com.bangkit.yourpetcare.konsultasi.chat_dokter.PesanActivity
import com.bangkit.yourpetcare.konsultasi.chat_dokter.PesanActivity.Companion.EXTRA_DATA
import com.bumptech.glide.Glide

class DetailDokterActivity : AppCompatActivity() {

    private var _binding : ActivityDetailDokterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailDokterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataDokter = intent.getParcelableExtra<Dokter>(EXTRA_DATA)
        if (dataDokter != null) {
            with(binding) {
                if (!dataDokter.image.isNullOrEmpty()) {
                    Glide.with(this@DetailDokterActivity)
                        .load(dataDokter.image)
                        .into(imgDokter)
                } else {
                    imgDokter.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.motorcycle, null))
                }
                tvNoStr.text = dataDokter.nomor
                tvRiwayatPendidikan.text = dataDokter.riwayatPendidikan
                tvTempatPraktik.text = dataDokter.tempatPraktik
                btnChat.setOnClickListener {
                    val intent = Intent(this@DetailDokterActivity, PesanActivity::class.java)
                    intent.putExtra(EXTRA_DATA, dataDokter)
                    startActivity(intent)
                }
            }
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}