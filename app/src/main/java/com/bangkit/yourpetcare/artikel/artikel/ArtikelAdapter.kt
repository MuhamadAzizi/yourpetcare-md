package com.bangkit.yourpetcare.artikel.artikel

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yourpetcare.databinding.ItemArtikelBinding
import com.bangkit.yourpetcare.databinding.ItemPembelianBinding
import com.bumptech.glide.Glide

class ArtikelAdapter : RecyclerView.Adapter<ArtikelAdapter.ViewHolder>() {
    private val artikelList = ArrayList<ArtikelModel>()

    fun setData (items:ArrayList<ArtikelModel>){
        artikelList.clear()
        artikelList.addAll(items)
        notifyDataSetChanged()
    }
    fun setDataDetail (items:ArrayList<ArtikelModel>){
        artikelList.clear()
        artikelList.addAll(items)
        artikelList.reverse()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding : ItemArtikelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model:ArtikelModel){
            Glide.with(itemView.context)
                .load(model.image)
                .into(binding.image)
            binding.tvJudulArtikelAwal.text = model.judul_artikel
            binding.tvDetailArtikelAwal.text = model.deskripsi

            binding.cardArtikel.setOnClickListener {
                val intent = Intent(itemView.context, ArtikelDetailActivity::class.java)
                intent.putExtra(ArtikelDetailActivity.EXTRA_ARTIKEL, model)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArtikelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(artikelList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = artikelList.size
}