package com.bangkit.yourpetcare.pembelian

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yourpetcare.databinding.ItemPembelianBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.NumberFormat

class TokoAdapter : RecyclerView.Adapter<TokoAdapter.ViewHolder>() {
    private val produkList = ArrayList<TokoModel>()

    @SuppressLint("NotifyDataChanged")
    fun setData(items:ArrayList<TokoModel>){
        produkList.clear()
        produkList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding : ItemPembelianBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model:TokoModel){
            val formatter: NumberFormat = DecimalFormat("#,###")

            Glide.with(itemView.context)
                .load(model.imgProduk)
                .into(binding.imgPembelian)
            binding.namaPembelian.text = model.namaProduk
            binding.pricePembelian.text = "Rp. ${formatter.format(model.hargaProduk)}"

            binding.cardProduk.setOnClickListener {
                val intent = Intent(itemView.context, DetailPembelianActivity::class.java)
                intent.putExtra(DetailPembelianActivity.EXTRA_PEMBELIAN, model)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPembelianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(produkList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = produkList.size
}