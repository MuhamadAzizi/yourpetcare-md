package com.bangkit.yourpetcare.konsultasi.chat_dokter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class PesanViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}