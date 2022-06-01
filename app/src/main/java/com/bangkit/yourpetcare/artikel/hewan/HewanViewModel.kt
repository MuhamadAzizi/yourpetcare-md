package com.bangkit.yourpetcare.artikel.hewan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class HewanViewModel : ViewModel() {

    private val hewanList = MutableLiveData<ArrayList<HewanModel>>()
    private val listItems = ArrayList<HewanModel>()
    private val TAG = HewanViewModel::class.java.simpleName

    fun setListHewan(){
        listItems.clear()

        try{
            FirebaseFirestore
                .getInstance()
                .collection("hewan")
                .get()
                .addOnSuccessListener {
                        documents ->
                    for (document in documents){
                        val model = HewanModel()
                        model.nama_hewan = document.data["nama_hewan"].toString()
                        model.deskripsi_hewan = document.data["deskripsi_hewan"].toString()
                        model.image_hewan = document.data["image_hewan"].toString()
                        model.uid = document.data["uid"].toString()

                        listItems.add(model)
                    }
                    hewanList.postValue(listItems)
                }.addOnFailureListener {
                        exception -> Log.w(TAG, "Error:", exception)
                }
        }catch (error: Exception){
            error.printStackTrace()
        }
    }
    fun getHewanList() : LiveData<ArrayList<HewanModel>> {
        return hewanList
    }
}