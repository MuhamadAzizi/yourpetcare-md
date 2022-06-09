package com.bangkit.yourpetcare.konsultasi.user_message

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityUserMessageBinding

class UserMessageActivity : AppCompatActivity() {

    private var _binding : ActivityUserMessageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserMessageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserMessageBinding.inflate(layoutInflater)
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