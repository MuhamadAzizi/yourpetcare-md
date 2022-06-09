package com.bangkit.yourpetcare.konsultasi.chat_dokter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.yourpetcare.konsultasi.chat_dokter.PesanAdapter.Companion.TYPE_RECEIVE_MESSAGE
import com.bangkit.yourpetcare.konsultasi.chat_dokter.PesanAdapter.Companion.TYPE_SENDER_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class PesanViewModel : ViewModel() {

    private val TAG = PesanViewModel::class.java.simpleName
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val senderUid = FirebaseAuth.getInstance().currentUser?.uid
    private var _allMessages = MutableLiveData<ArrayList<ItemPesan>>()
    val allMessages: LiveData<ArrayList<ItemPesan>> = _allMessages

    fun getMessage(senderRoomUid: String) {
        viewModelScope.launch {
            firebaseFirestore.collection("chats")
                .document(senderRoomUid)
                .collection("messages")
                .addSnapshotListener{ documents, _ ->
                    val mMessages = arrayListOf<ItemPesan>()
                    if (documents != null) {
                        _allMessages.value?.clear()
                        for (document in documents) {
                            if (!document.data["senderUid"].toString().equals(senderUid, ignoreCase = true)) {
                                mMessages.add(
                                    ItemPesan(
                                        content = document.data["content"].toString(),
                                        tipePesan = TYPE_RECEIVE_MESSAGE
                                    )
                                )
                            } else {
                                mMessages.add(
                                    ItemPesan(
                                        content = document.data["content"].toString(),
                                        tipePesan = TYPE_SENDER_MESSAGE
                                    )
                                )
                            }
                        }
                    }
                    Log.d(TAG, "getSenderRoom: $mMessages")
                    _allMessages.postValue(mMessages)
                }
        }
    }

    fun sendMessage(
        senderRoomUid: String,
        receiverRoomUid: String,
        senderUid: String,
        contentMessage: String
    ) {
        val currentTimeMillis = System.currentTimeMillis().toString()
        val messages = mapOf(
            "content" to contentMessage,
            "senderUid" to senderUid
        )
        viewModelScope.launch {
            try {
                firebaseFirestore.collection("chats")
                    .document(senderRoomUid)
                    .collection("messages")
                    .document(currentTimeMillis)
                    .set(messages)
                    .addOnSuccessListener {
                        Log.d(TAG, "sendMessage: Sukses mengirim pesan sender")
                        firebaseFirestore.collection("chats")
                            .document(receiverRoomUid)
                            .collection("messages")
                            .document(currentTimeMillis)
                            .set(messages)
                            .addOnSuccessListener {
                                Log.d(TAG, "sendMessage: Sukses mengirim pesan receiver")
                            }
                            .addOnFailureListener {
                                Log.d(TAG, "sendMessage: gagal mengirim pesan receiver")
                            }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "sendMessage: ${exception.message}")
                    }
            } catch (e: Exception) {
                throw IllegalArgumentException(e.message)
            }
        }
    }
}