package com.bangkit.yourpetcare.pembelian.keranjang

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.databinding.ActivityKeranjangBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat
import java.text.NumberFormat

class KeranjangActivity : AppCompatActivity() {

    private var binding : ActivityKeranjangBinding? = null
    private var adapter : KeranjangAdapter? = null
    private var formater : NumberFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeranjangBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.imageButton?.setOnClickListener {
            onBackPressed()
        }
        binding?.pesan?.setOnClickListener {
            startActivity(Intent(this, DetailKeranjangActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        val subTotalPrice = binding?.textView7
        val pesanButton = binding?.pesan

        val progressDialog = ProgressDialog(this)


        binding?.recyclerviewKeranjang?.layoutManager = LinearLayoutManager(this)
        adapter = KeranjangAdapter(subTotalPrice, pesanButton, "cart")
        binding?.recyclerviewKeranjang?.adapter = adapter

        val viewModel = ViewModelProvider(this)[KeranjangViewModel::class.java]
        binding?.progressbar?.visibility = View.VISIBLE

        viewModel.setListCart()
        viewModel.getCartList().observe(this){
            cart->
            if(cart.size > 0){
                progressDialog.setMessage("Harap ditunggu hingga proses selesai")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.dismiss()
                binding?.progressbar?.visibility = View.GONE
                binding?.noData?.visibility = View.GONE
                binding?.pesan?.visibility = View.VISIBLE
                adapter!!.setData(cart)
                subtotal()
            }else{
                binding?.pesan?.visibility = View.GONE
                binding?.progressbar?.visibility = View.GONE
                binding?.noData?.visibility = View.VISIBLE
                binding?.textView7?.text = "Rp. 0"
            }
        }
    }

    private fun subtotal() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var totalPrice = 0L
        FirebaseFirestore
            .getInstance()
            .collection("cart")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { document->
                for (i in document){
                    totalPrice = totalPrice.plus(i.data["price"] as Long)
                }
                binding?.textView7?.text = "Rp. ${formater.format(totalPrice)}"
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }
}