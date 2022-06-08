package com.bangkit.yourpetcare.konsultasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class KonsultasiViewModel : ViewModel(){
    private val user = MutableLiveData<ArrayList<UserModel>>()
    private val listUser = ArrayList<UserModel>()
    private val TAG = KonsultasiViewModel::class.java.simpleName

    fun setListUser(){

        listUser.clear()

        try {
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .whereEqualTo("role", "dokter")
                .get()
                .addOnSuccessListener {
                    documents -> for (document in documents){
                        val model = UserModel()
                        model.username = document.data["username"].toString()
                        model.image = document.data["image"].toString()
                        model.email = document.data["email"].toString()
                        model.no_str = document.data["no_str"].toString()
                        model.spesialis = document.data["spesialis"].toString()
                        model.riwayat_pendidikan = document.data["riwayat_pendidikan"].toString()
                        model.tempat_praktik = document.data["tempat_praktik"].toString()
                        model.uid = document.data["uid"].toString()

                        listUser.add(model)
                    }
                    user.postValue(listUser)
                }
        }catch (error : Exception){
            error.printStackTrace()
        }
    }

    fun getUserList() : LiveData<ArrayList<UserModel>>{
        return user
    }
}