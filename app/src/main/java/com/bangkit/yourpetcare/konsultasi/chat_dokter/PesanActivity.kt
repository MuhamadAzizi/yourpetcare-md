package com.bangkit.yourpetcare.konsultasi.chat_dokter

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityPesanBinding
import com.bangkit.yourpetcare.konsultasi.Dokter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class PesanActivity : AppCompatActivity() {

    private var _binding: ActivityPesanBinding? = null
    private val binding get() = _binding!!
    private lateinit var messageList: ArrayList<ItemPesan>
    private lateinit var pesanAdapter: PesanAdapter
    private val viewModel: PesanViewModel by viewModels()
    private var senderRoomUid: String? = null
    private var receiverRoomUid: String? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPesanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_blue_24)

        val receiverUid = intent.getParcelableExtra<Dokter>(EXTRA_DATA)?.uid
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoomUid = senderUid + receiverUid
        receiverRoomUid = receiverUid + senderUid


        messageList = arrayListOf()

        lifecycleScope.launch {
            viewModel.getMessage(senderRoomUid!!)
            viewModel.allMessages.observe(this@PesanActivity) {
                pesanAdapter = PesanAdapter(it)
                binding.rvPesan.layoutManager = LinearLayoutManager(this@PesanActivity)
                binding.rvPesan.adapter = pesanAdapter
                binding.rvPesan.scrollToPosition(it.size-1)
                pesanAdapter.notifyDataSetChanged()
            }
        }

        binding.btnSend.setOnClickListener {
            val contentMessage = binding.edtTextType.text.toString().trim()
            lifecycleScope.launch {
                viewModel.sendMessage(
                    senderRoomUid = senderRoomUid!!,
                    receiverRoomUid = receiverRoomUid!!,
                    senderUid = senderUid!!,
                    contentMessage = contentMessage
                )
            }
            binding.edtTextType.text.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_DATA = "pesan_extra_data"
        val TAG = PesanActivity::class.java.simpleName
    }
}