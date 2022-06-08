package com.bangkit.yourpetcare.konsultasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.FragmentKonsultasiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class KonsultasiFragment : Fragment() {

    private var binding : FragmentKonsultasiBinding? = null
    private var adapter : KonsultasiAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKonsultasiBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        initRecyclerViewDokter()
    }


    private fun initRecyclerViewDokter() {
        binding?.recyclerViewDokter?.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL, false)

        adapter = KonsultasiAdapter()
        binding?.recyclerViewDokter?.adapter = adapter

        val viewModelKonsultasi = ViewModelProvider(this)[KonsultasiViewModel::class.java]
        binding?.progressbarKonsultasi?.visibility = View.VISIBLE
        binding?.nodata?.visibility = View.VISIBLE
        viewModelKonsultasi.setListUser()
        viewModelKonsultasi.getUserList()
            .observe(viewLifecycleOwner){
                konsultasi -> if(konsultasi.size > 0){
                    adapter!!.setData(konsultasi)
                }
                binding?.progressbarKonsultasi?.visibility = View.GONE
                binding?.nodata?.visibility = View.GONE
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}