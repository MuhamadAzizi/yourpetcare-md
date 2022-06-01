package com.bangkit.yourpetcare.artikel.hewan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yourpetcare.artikel.artikel.ArtikelModel
import com.bangkit.yourpetcare.databinding.ItemGridBinding
import com.bangkit.yourpetcare.databinding.ItemHewanBinding
import com.bumptech.glide.Glide

class HewanAdapterGrid : RecyclerView.Adapter<HewanAdapterGrid.ViewHolder>() {

    private val hewanList = ArrayList<HewanModel>()

    fun setData (items:ArrayList<HewanModel>){
        hewanList.clear()
        hewanList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding : ItemGridBinding)
        : RecyclerView.ViewHolder(binding.root)  {
        fun bind(model: HewanModel) {
            Glide.with(itemView.context)
                .load(model.image_hewan)
                .into(binding.imgCardHewan)
            binding.tvHewan.text = model.nama_hewan
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(hewanList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = hewanList.count()
}