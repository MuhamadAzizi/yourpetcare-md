package com.bangkit.yourpetcare.konsultasi

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ItemDokterBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class KonsultasiAdapter : RecyclerView.Adapter<KonsultasiAdapter.ViewHolder>() {

    private val userList = ArrayList<UserModel>()

    @SuppressLint("NotifyDataChanged")
    fun setData(items : ArrayList<UserModel>){
        userList.clear()
        userList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding:ItemDokterBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(userModel: UserModel) {
            Glide.with(itemView.context)
                .load(userModel.image)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(binding.imgDokter)
            binding.tvNamadokter.text = userModel.username
            binding.tvSpesialis.text = userModel.spesialis


            binding.cardDokter.setOnClickListener {
                val intent = Intent(itemView.context, KonsultasiDetailActivity::class.java)
                intent.putExtra(KonsultasiDetailActivity.EXTRA_DOKTER, userModel)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDokterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = userList.size
}