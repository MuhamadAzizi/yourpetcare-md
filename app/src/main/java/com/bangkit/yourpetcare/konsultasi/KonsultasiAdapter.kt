package com.bangkit.yourpetcare.konsultasi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ItemListDokterBinding

class KonsultasiAdapter(private val listDokter: ArrayList<Dokter>) : RecyclerView.Adapter<KonsultasiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KonsultasiAdapter.ViewHolder {
        val view = ItemListDokterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: KonsultasiAdapter.ViewHolder, position: Int) {
        holder.bind(listDokter[position])
    }

    override fun getItemCount(): Int = listDokter.size

    inner class ViewHolder(private val binding: ItemListDokterBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(dokter: Dokter) {
            binding.imgDokter.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources, R.drawable.motorcycle, null))
            binding.tvNamadokter.text = dokter.username
        }
    }
}