package com.bangkit.yourpetcare.pembelian.obat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bangkit.yourpetcare.databinding.FragmentObatBinding
import com.bangkit.yourpetcare.pembelian.TokoAdapter
import com.bangkit.yourpetcare.pembelian.ViewModelToko
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ObatFragment : Fragment() {

    private var binding : FragmentObatBinding? = null
    private var role : String? = null
    private var adapterToko : TokoAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObatBinding.inflate(layoutInflater, container, false)
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
                    binding?.addObat?.visibility = View.VISIBLE
                }
            }
    }

    override fun onResume() {
        super.onResume()
        initRecyclerview()
        showListObat()
    }

    private fun initRecyclerview() {
        binding?.recyclerviewObat?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        adapterToko = TokoAdapter()
        binding?.recyclerviewObat?.adapter = adapterToko
    }

    private fun showListObat() {
        val viewModel = ViewModelProvider(this)[ViewModelToko::class.java]
        binding?.progressbarObat?.visibility  = View.VISIBLE
        viewModel.setListObat()
        viewModel.getProdukList().observe(this){ produk ->
            if(produk.size > 0){
            adapterToko!!.setData(produk)
        }
            binding?.progressbarObat?.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.addObat?.setOnClickListener {
            startActivity(Intent(activity, AddDataObatActivity::class.java))
        }

    }

}