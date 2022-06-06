package com.bangkit.yourpetcare.pembelian.pesanan

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yourpetcare.databinding.ItemPemesananBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.NumberFormat

class PesananAdapter : RecyclerView.Adapter<PesananAdapter.ViewHolder>() {

    private val pesananList = ArrayList<OrderModel>()

    @SuppressLint("NotifyDataChanged")
    fun setData(items : ArrayList<OrderModel>){
        pesananList.clear()
        pesananList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding : ItemPemesananBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderModel: OrderModel) {
            val formatter:NumberFormat = DecimalFormat("#,###")
            with(binding){
//                Glide
//                    .with(itemView.context)
//                    .load(orderModel.img)
//                    .into(circleImageView)

                orderid.text = "Order Id : ABC - ${orderModel.orderId}"
                username.text = "Pembeli: ${orderModel.username}"
                nominal.text = "Total: Rp. ${formatter.format(orderModel.totalPrice)}"
                waktu.text = "Tanggal: ${orderModel.date}"

                cardPemesanan.setOnClickListener {
                    val intent = Intent(itemView.context, DetailPesananActivity::class.java)
                    intent.putExtra(DetailPesananActivity.EXTRA_ORDER, orderModel)
                    itemView.context.startActivity(intent)
                }
            }



        }

    }

    private val produkList = ArrayList<OrderModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPemesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(pesananList[position])
    }

    override fun getItemCount(): Int = pesananList.size


}
