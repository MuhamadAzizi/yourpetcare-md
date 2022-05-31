package com.bangkit.yourpetcare.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.yourpetcare.databinding.ActivityLoginBinding
import com.bangkit.yourpetcare.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private var binding : ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.tvBlmpunyaakun?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }
}