package com.bangkit.yourpetcare.pembelian.pesanan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.FragmentPesananBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PesananFragment : Fragment() {

    private var binding : FragmentPesananBinding? = null
    private var adapter : PesananAdapter? = null
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onResume() {
        super.onResume()
        checkRole()
    }

    private fun checkRole() {
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it.data?.get("role") == "user"){
                    initProcessOrder("user")
                }else{
                    initProcessOrder("admin")

                }
            }
    }

    private fun initProcessOrder(s: String) {
        val viewModel = ViewModelProvider(this)[PesananViewModel::class.java]
        binding?.progressbarPesanan?.visibility = View.VISIBLE
        binding?.noData?.visibility = View.VISIBLE
        binding?.recyclerviewPesanan?.layoutManager = LinearLayoutManager(activity)
        adapter = PesananAdapter()
        binding?.recyclerviewPesanan?.adapter = adapter

        if(s == "user"){
            viewModel.setPemesananAll()
        }else{
            viewModel.setPemesananBelumBayarId(uid)
        }

        viewModel.getOrderList().observe(viewLifecycleOwner){
            process-> if(process.size > 0){
                binding?.progressbarPesanan?.visibility = View.GONE
                binding?.noData?.visibility = View.GONE
                adapter!!.setData(process)
            }else{
                binding?.progressbarPesanan?.visibility = View.VISIBLE
                binding?.noData?.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPesananBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}