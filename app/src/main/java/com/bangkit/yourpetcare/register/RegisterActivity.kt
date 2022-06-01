package com.bangkit.yourpetcare.register

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityRegisterBinding
import com.bangkit.yourpetcare.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private var binding : ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tvAdaakun?.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        binding?.register?.setOnClickListener {
            formValidation()
        }
    }

    private fun formValidation(){
        val username = binding?.usernameRegist?.text.toString().trim()
        val email = binding?.emailRegist?.text.toString().trim()
        val password = binding?.passwordRegist?.text.toString().trim()

        if(username.isEmpty()){
            Toast.makeText(this, "Username jangan kosong", Toast.LENGTH_SHORT).show()
            return
        }else if(email.isEmpty()){
            Toast.makeText(this, "Email jangan kosong", Toast.LENGTH_SHORT).show()
            return
        }else if(password.isEmpty()){
            Toast.makeText(this, "Password jangan kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mohon ditunggu terlebih dahulu")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()


        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val uid = FirebaseAuth.getInstance().currentUser!!.uid
                    val data = mapOf(
                        "uid" to uid,
                        "username" to username,
                        "email" to email,
                        "password" to password,
                        "role" to "user"
                    )

                    uid.let {
                        data2-> FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(data2)
                        .set(data)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                progressDialog.dismiss()
                                successDialog()
                            }else{
                                progressDialog.dismiss()
                                failureDialog()
                            }
                        }
                    }

                }else{
                    progressDialog.dismiss()
                    failureDialog()

                }
            }
    }

    private fun failureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Daftar")
            .setMessage("Terdapat kesalahan saat melakukan pendaftaran, silahkan cek koneksi internet anda")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("Oke"){
                dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun successDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Daftar")
            .setMessage("Selamat anda berhasil mendaftar, silahkan login")
            .setIcon(R.drawable.ic_baseline_check_24)
            .setPositiveButton("Oke"){
                dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, LoginActivity::class.java))
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }

}