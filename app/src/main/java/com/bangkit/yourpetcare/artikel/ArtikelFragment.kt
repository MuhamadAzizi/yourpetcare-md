package com.bangkit.yourpetcare.artikel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.artikel.artikel.ArtikelAdapter
import com.bangkit.yourpetcare.artikel.artikel.ArtikelAddActivity
import com.bangkit.yourpetcare.artikel.artikel.ArtikelViewModel
import com.bangkit.yourpetcare.artikel.hewan.HewanAdapter
import com.bangkit.yourpetcare.artikel.hewan.HewanAddActivity
import com.bangkit.yourpetcare.artikel.hewan.HewanGridActivity
import com.bangkit.yourpetcare.artikel.hewan.HewanViewModel
import com.bangkit.yourpetcare.databinding.FragmentArtikelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ArtikelFragment : Fragment() {

    private var binding:FragmentArtikelBinding? = null
    private var role : String? = null
    private var hewanAdapter : HewanAdapter? = null
    private var artikelAdapter : ArtikelAdapter? = null

    override fun onResume() {
        super.onResume()
        showHewan()
        showArtikel()
    }

    private fun showArtikel() {
        binding?.recyclerviewBerita?.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.VERTICAL,false
        )
        artikelAdapter = ArtikelAdapter()
        binding?.recyclerviewBerita?.adapter = artikelAdapter

        val viewModel = ViewModelProvider(this)[ArtikelViewModel::class.java]
        binding?.progressbarArtikel?.visibility  = View.VISIBLE
        viewModel.setListArtikel()
        viewModel.getArtikelList().observe(viewLifecycleOwner){
                artikel -> if(artikel.size > 0){
            artikelAdapter!!.setData(artikel)
        }
            binding?.progressbarArtikel?.visibility = View.GONE
        }
    }

    private fun showHewan() {
        binding?.recyclerviewHewan?.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL, false
        )
        hewanAdapter = HewanAdapter()
        binding?.recyclerviewHewan?.adapter = hewanAdapter

        val viewModel = ViewModelProvider(this)[HewanViewModel::class.java]
        binding?.progressbarHewan?.visibility  = View.VISIBLE
        viewModel.setListHewan()
        viewModel.getHewanList().observe(viewLifecycleOwner){
                hewan -> if(hewan.size > 0){
            hewanAdapter!!.setData(hewan)
        }
            binding?.progressbarHewan?.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArtikelBinding.inflate(layoutInflater, container,false)
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
                    binding?.addArtikel?.visibility = View.VISIBLE
                    binding?.addHewan?.visibility = View.VISIBLE
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.addHewan?.setOnClickListener {
            startActivity(Intent(activity, HewanAddActivity::class.java))
        }
        binding?.addArtikel?.setOnClickListener {
            startActivity(Intent(activity,ArtikelAddActivity::class.java))
        }

        binding?.lihatsemua?.setOnClickListener {
            startActivity(Intent(activity,HewanGridActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}