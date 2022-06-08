package com.bangkit.yourpetcare.konsultasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityKonsultasiDetailBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class KonsultasiDetailActivity : AppCompatActivity() {


    private var binding : ActivityKonsultasiDetailBinding? = null
    private var model : UserModel? = null
    private var role : String? = null

    companion object{
        const val EXTRA_DOKTER = "dokter"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKonsultasiDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        checkRole()

        binding?.editDokter?.setOnClickListener {
            val intent = Intent(this, EditKonsultasiActivity::class.java)
            intent.putExtra(EditKonsultasiActivity.EXTRA_EDIT, model)
            startActivity(intent)
        }

        model = intent.getParcelableExtra(EXTRA_DOKTER)

        Glide.with(this)
            .load(model?.image)
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(binding!!.imgDokterDetail)

        binding?.namaDokter?.text = model?.username
        binding?.spesialisDokter?.text = model?.spesialis
        binding?.noStrDetail?.text = model?.no_str
        binding?.lulusanDetail?.text = model?.riwayat_pendidikan
        binding?.tempatpraktikDetail?.text = model?.tempat_praktik


        binding?.chatDokter?.setOnClickListener {
            val intent = Intent(this, KonsultasiActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkRole() {
        val uId = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uId)
            .get()
            .addOnSuccessListener {
                role = "" + it.data?.get("role")
                if(role == "admin")
                    binding?.editDokter?.visibility = View.VISIBLE
            }
    }
}
