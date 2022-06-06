package com.bangkit.yourpetcare.pembelian.shop

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bangkit.yourpetcare.databinding.FragmentShopBinding
import com.bangkit.yourpetcare.pembelian.TokoAdapter
import com.bangkit.yourpetcare.pembelian.ViewModelToko
import com.bangkit.yourpetcare.pembelian.obat.AddDataObatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ShopFragment : Fragment() {

    private var binding : FragmentShopBinding? = null
    private var role : String? = null
    private var adapterToko : TokoAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentShopBinding.inflate(layoutInflater, container, false)
        checkRole()
        return binding?.root
    }

    private fun checkRole() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                role = "" + it.data?.get("role")
                if(role == "admin"){
                    binding?.addShop?.visibility = View.VISIBLE
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.addShop?.setOnClickListener {
            startActivity(Intent(activity, AddDataShopActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        initRecyclerview()
        showListShop()
    }

    private fun showListShop() {
        val viewModel = ViewModelProvider(this)[ViewModelToko::class.java]
        binding?.pbShop?.visibility  = View.VISIBLE
        viewModel.setListPet()
        viewModel.getProdukList().observe(this){ produk ->
            if(produk.size > 0){
                adapterToko!!.setData(produk)
            }
            binding?.pbShop?.visibility = View.GONE
        }
    }

    private fun initRecyclerview() {
        binding?.recyclerView?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        adapterToko = TokoAdapter()
        binding?.recyclerView?.adapter = adapterToko
    }


}