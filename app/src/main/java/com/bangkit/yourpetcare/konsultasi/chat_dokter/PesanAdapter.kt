package com.bangkit.yourpetcare.konsultasi.chat_dokter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yourpetcare.R

class PesanAdapter(private val data: ArrayList<ItemPesan>) : RecyclerView.Adapter<PesanViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesanViewHolder<*> {
        val context = parent.context
        return when (viewType) {
            TYPE_SENDER_MESSAGE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_text_sender, parent,false)
                SenderMessageViewHolder(view)
            }
            TYPE_RECEIVE_MESSAGE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_text_receiver, parent, false)
                ReceiverMessageViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("Invalid view type!")
            }
        }
    }

    override fun onBindViewHolder(holder: PesanViewHolder<*>, position: Int) {
        val item = data[position]
        when (holder) {
            is SenderMessageViewHolder -> holder.bind(item)
            is ReceiverMessageViewHolder -> holder.bind(item)
            else -> throw IllegalArgumentException("Invalid holder!")
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int) = data[position].tipePesan

    class SenderMessageViewHolder(val view: View) : PesanViewHolder<ItemPesan>(view) {
        private val message = view.findViewById<TextView>(R.id.message)
        override fun bind(item: ItemPesan) {
            message.text = item.content
        }
    }

    class ReceiverMessageViewHolder(val view: View) : PesanViewHolder<ItemPesan>(view) {
        private val message = view.findViewById<TextView>(R.id.message)
        override fun bind(item: ItemPesan) {
            message.text = item.content
        }

    }

    companion object {
        const val TYPE_SENDER_MESSAGE = 0
        const val TYPE_RECEIVE_MESSAGE = 1
    }
}