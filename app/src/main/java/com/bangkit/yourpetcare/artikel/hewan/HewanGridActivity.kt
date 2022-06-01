package com.bangkit.yourpetcare.artikel.hewan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.artikel.artikel.ArtikelAdapter
import com.bangkit.yourpetcare.databinding.ActivityHewanGridBinding

class HewanGridActivity : AppCompatActivity() {

    private var binding: ActivityHewanGridBinding? = null
    private var hewanAdapter : HewanAdapterGrid? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHewanGridBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        showHewan()
    }

    private fun showHewan() {
        binding?.recyclerviewHewanGrid?.layoutManager = GridLayoutManager(
            this, 2
        )
        hewanAdapter = HewanAdapterGrid()
        binding?.recyclerviewHewanGrid?.adapter = hewanAdapter

        val viewModel = ViewModelProvider(this)[HewanViewModel::class.java]
        binding?.progressbarHewan?.visibility  = View.VISIBLE
        viewModel.setListHewan()
        viewModel.getHewanList().observe(this){
                hewan -> if(hewan.size > 0){
            hewanAdapter!!.setData(hewan)
        }
            binding?.progressbarHewan?.visibility = View.GONE
        }
    }
}