package com.bangkit.yourpetcare.konsultasi.detail_dokter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityDetailDokterBinding
import com.bangkit.yourpetcare.konsultasi.Dokter
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
            }
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_DATA = "detail_dokter_extra_data"
        val TAG = DetailDokterActivity::class.java.simpleName
    }
}