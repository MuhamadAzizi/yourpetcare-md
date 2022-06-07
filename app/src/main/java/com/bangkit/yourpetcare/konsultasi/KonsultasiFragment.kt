package com.bangkit.yourpetcare.konsultasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.databinding.FragmentKonsultasiBinding
import com.bangkit.yourpetcare.utils.DataDummy

class KonsultasiFragment : Fragment() {

    private var _binding: FragmentKonsultasiBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: KonsultasiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKonsultasiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        adapter = KonsultasiAdapter(DataDummy.dummyDoctors())
        binding.rvDokter.layoutManager = LinearLayoutManager(context)
        binding.rvDokter.adapter = adapter
        binding.rvDokter.setHasFixedSize(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}