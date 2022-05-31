package com.bangkit.yourpetcare.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityRegisterBinding
import com.bangkit.yourpetcare.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private var binding : ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tvAdaakun?.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }


    }
}