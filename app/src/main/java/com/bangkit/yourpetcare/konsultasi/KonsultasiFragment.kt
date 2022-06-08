package com.bangkit.yourpetcare.konsultasi

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.databinding.FragmentKonsultasiBinding
import com.bangkit.yourpetcare.konsultasi.detail_dokter.DetailDokterActivity
import com.bangkit.yourpetcare.utils.DataDummy

class KonsultasiFragment : Fragment() {

    private var _binding: FragmentKonsultasiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: KonsultasiViewModel by viewModels()

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
        viewModel.dokterList.observe(viewLifecycleOwner) { data ->
            showData(data)
            binding.edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!p0.isNullOrEmpty()) {
                        val searchDataDokter = data.filter { it.username!!.contains(p0, ignoreCase = true) }
                        showData(searchDataDokter)
                    } else {
                        showData(data)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}

            })
        }
    }

    private fun showData(dataDokter: List<Dokter>) {
        adapter = KonsultasiAdapter(dataDokter.toCollection(arrayListOf()))
        binding.rvDokter.layoutManager = LinearLayoutManager(context)
        binding.rvDokter.adapter = adapter
        binding.rvDokter.setHasFixedSize(true)
        adapter.onItemClicked = { dokter ->
            val intent = Intent(context, DetailDokterActivity::class.java)
            intent.putExtra(DetailDokterActivity.EXTRA_DATA, dokter)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}