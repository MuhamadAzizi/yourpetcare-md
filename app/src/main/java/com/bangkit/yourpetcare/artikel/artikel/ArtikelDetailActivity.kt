package com.bangkit.yourpetcare.artikel.artikel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.artikel.ArtikelFragment
import com.bangkit.yourpetcare.databinding.ActivityDetailBeritaBinding
import com.bumptech.glide.Glide

class ArtikelDetailActivity : AppCompatActivity() {

    private var binding : ActivityDetailBeritaBinding? = null
    private var model : ArtikelModel? = null
    private var artikelAdapter : ArtikelAdapter? = null

    companion object{
        const val EXTRA_ARTIKEL = "artikel"
    }

    override fun onResume() {
        super.onResume()

        model = intent.getParcelableExtra(EXTRA_ARTIKEL)
        Glide.with(this)
            .load(model?.image)
            .into(binding!!.imgDetailBerita)
        binding?.tvBeritaDetail?.text = model?.judul_artikel
        binding?.tvDeskripsiDetailBerita?.text = model?.deskripsi
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBeritaBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        showBeritaLainnya()
    }

    private fun showBeritaLainnya() {
        binding?.recyclerviewDetailBerita?.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,false
        )
        artikelAdapter = ArtikelAdapter()
        binding?.recyclerviewDetailBerita?.adapter = artikelAdapter

        val viewModel = ViewModelProvider(this)[ArtikelViewModel::class.java]
        binding?.progressbarDetailArtikel?.visibility  = View.VISIBLE
        viewModel.setListArtikel()
        viewModel.getArtikelList().observe(this){
                artikel -> if(artikel.size > 0){
            artikelAdapter!!.setDataDetail(artikel)
        }
            binding?.progressbarDetailArtikel?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}