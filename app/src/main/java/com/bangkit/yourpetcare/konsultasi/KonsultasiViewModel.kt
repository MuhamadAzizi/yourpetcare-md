package com.bangkit.yourpetcare.konsultasi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.yourpetcare.utils.ConstDatabase.USERS_COLLECTION
import com.bangkit.yourpetcare.utils.ConstDatabase.USER_EMAIL
import com.bangkit.yourpetcare.utils.ConstDatabase.USER_IMAGE
import com.bangkit.yourpetcare.utils.ConstDatabase.USER_NO_STR
import com.bangkit.yourpetcare.utils.ConstDatabase.USER_RIWAYAT_PENDIDIKAN
import com.bangkit.yourpetcare.utils.ConstDatabase.USER_ROLE
import com.bangkit.yourpetcare.utils.ConstDatabase.USER_SPESIALIS
import com.bangkit.yourpetcare.utils.ConstDatabase.USER_TEMPAT_PRAKTIK
import com.bangkit.yourpetcare.utils.ConstDatabase.USER_UID
import com.bangkit.yourpetcare.utils.ConstDatabase.USER_USERNAME
import com.google.firebase.firestore.FirebaseFirestore

class KonsultasiViewModel : ViewModel() {

    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private var _dokterList = MutableLiveData<List<Dokter>>()
    private val TAG = KonsultasiViewModel::class.java.simpleName
    val dokterList : LiveData<List<Dokter>> = _dokterList

    init {
        getDokterList()
    }

    private fun getDokterList() {
        try {
            val dataDokter = ArrayList<Dokter>()
            firebaseFirestore.collection(USERS_COLLECTION)
                .whereEqualTo(USER_ROLE, "dokter")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val dokter = Dokter(
                            email = document.data[USER_EMAIL].toString(),
                            username = document.data[USER_USERNAME].toString(),
                            nama = "default",
                            nomor = document.data[USER_NO_STR].toString(),
                            riwayatPendidikan = document.data[USER_RIWAYAT_PENDIDIKAN].toString(),
                            tempatPraktik = document.data[USER_TEMPAT_PRAKTIK].toString(),
                            uid = document.data[USER_UID].toString(),
                            spesialis = document.data[USER_SPESIALIS].toString(),
                            image = document.data[USER_IMAGE].toString()
                        )
                        dataDokter.add(dokter)
                    }
                    _dokterList.postValue(dataDokter)
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "getDokterList: ${exception.message}")
                }
        } catch (e: Exception) {
            throw IllegalArgumentException("getDokterlist ${e.message}")
        }
    }

}