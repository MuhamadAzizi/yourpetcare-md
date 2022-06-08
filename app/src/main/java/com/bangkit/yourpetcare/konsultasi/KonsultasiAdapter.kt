package com.bangkit.yourpetcare.konsultasi

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ItemListDokterBinding
import com.bangkit.yourpetcare.konsultasi.chat_dokter.PesanActivity
import com.bumptech.glide.Glide

class KonsultasiAdapter(private val listDokter: ArrayList<Dokter>) : RecyclerView.Adapter<KonsultasiAdapter.ViewHolder>() {

    var onItemClicked: ((Dokter) -> Unit)? = null

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
            if (!dokter.image.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load(dokter.image)
                    .into(binding.imgDokter)
            } else {
                binding.imgDokter.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources, R.drawable.motorcycle, null))
            }
            binding.tvNamadokter.text = dokter.username
            binding.btnChat.setOnClickListener {
                val intent = Intent(itemView.context, PesanActivity::class.java)
                itemView.context.startActivity(intent)
            }
            itemView.setOnClickListener {
                onItemClicked?.invoke(dokter)
            }
        }
    }
}