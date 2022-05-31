package com.bangkit.yourpetcare.login

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.beranda.MainActivity
import com.bangkit.yourpetcare.databinding.ActivityLoginBinding
import com.bangkit.yourpetcare.register.RegisterActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private var binding : ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        autoLogin()

        binding?.tvBlmpunyaakun?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding?.login?.setOnClickListener {
            formValidation()
        }
    }

    private fun autoLogin() {
        if(FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun formValidation() {
        var email = binding?.emailLogin?.text.toString().trim()
        var password = binding?.passwordLogin?.text.toString().trim()

        if(email.isEmpty()){
            Toast.makeText(this, "Email jangan dikosongkan", Toast.LENGTH_SHORT).show()
            return
        }else if(password.isEmpty()){
            Toast.makeText(this, "Password jangan dikosongkan", Toast.LENGTH_SHORT).show()
            return
        }


        val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Mohon ditunggu terlebih dahulu")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

        FirebaseFirestore
            .getInstance()
            .collection("users")
            .get()
            .addOnCompleteListener(OnCompleteListener{task ->
                if(task.result.size() == 0){
                    progressDialog.dismiss()
                    failureDialog()
                    return@OnCompleteListener
                    }

                for(snapshot in task.result){
                    val email = "" + snapshot ["email"]
                    FirebaseAuth
                        .getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if(task.isSuccessful){
                                progressDialog.dismiss()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }else {
                                progressDialog.dismiss()
                                failureDialog()
                            }                        }
                    }
                }
            )
    }

    private fun failureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Login")
            .setMessage("Selamat anda berhasil login")
            .setIcon(R.drawable.ic_baseline_check_24)
            .setPositiveButton("oke"){
                dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun successDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Login")
            .setMessage("Mohon maaf, anda gagal login, cek koneksi internet anda")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("oke"){
                dialog, _ ->
                dialog.dismiss()
                onBackPressed()
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding
    }
}
