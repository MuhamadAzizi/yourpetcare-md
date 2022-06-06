package com.bangkit.yourpetcare.pembelian.keranjang

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yourpetcare.databinding.ItemKeranjangBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat
import java.text.NumberFormat

class KeranjangAdapter (
    private val subTotalPrice:TextView?,
    private val checkoutButton : Button?,
    private val option : String
): RecyclerView.Adapter<KeranjangAdapter.ViewHolder>(){

    private val cartList = ArrayList<KeranjangModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items : ArrayList<KeranjangModel>){
        cartList.clear()
        cartList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding : ItemKeranjangBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun bind(keranjangModel: KeranjangModel) {
            val formater : NumberFormat = DecimalFormat("#,###")

            with(binding){
                Glide.with(itemView.context)
                    .load(keranjangModel.image)
                    .into(imgProduk)
                tvNamaProdukKeranjang.text = keranjangModel.name
                tvEntityKeranjang.text = "Jumlah ${keranjangModel.qty.toString()} buah"
                tvPriceKeranjang.text = "Rp. ${formater.format(keranjangModel.price)}"

                if(option == "cart"){
                    delete.visibility = View.VISIBLE
                }

                delete.setOnClickListener {
                    keranjangModel.cartId?.let{
                        it1->FirebaseFirestore
                        .getInstance()
                        .collection("cart")
                        .document(it1)
                        .delete()
                        .addOnCompleteListener {
                            task->
                            if(task.isSuccessful){
                                cartList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                if(cartList.size>0){
                                    var subTotal = 0L
                                    for(i in cartList.indices){
                                        subTotal += cartList[i].price!!
                                    }
                                    subTotalPrice?.text = "Rp. ${formater.format(subTotal)}"
                                }else{
                                    checkoutButton?.visibility = View.INVISIBLE
                                    subTotalPrice?.text = "Rp. 0"
                                }
                            }else{
                                Toast.makeText(itemView.context, "Gagal menghapus produk", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKeranjangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartList[position])
    }

    override fun getItemCount(): Int = cartList.size
}