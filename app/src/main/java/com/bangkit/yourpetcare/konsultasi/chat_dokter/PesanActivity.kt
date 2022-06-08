package com.bangkit.yourpetcare.konsultasi.chat_dokter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityPesanBinding

class PesanActivity : AppCompatActivity() {

    private var _binding: ActivityPesanBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPesanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_blue_24)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}