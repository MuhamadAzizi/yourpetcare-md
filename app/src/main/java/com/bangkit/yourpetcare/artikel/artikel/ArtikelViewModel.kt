package com.bangkit.yourpetcare.artikel.artikel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class ArtikelViewModel : ViewModel() {

    private val artikelList = MutableLiveData<ArrayList<ArtikelModel>>()
    private val listItems = ArrayList<ArtikelModel>()
    private val TAG = ArtikelViewModel::class.java.simpleName

    fun setListArtikel(){
        listItems.clear()

        try{
            FirebaseFirestore
                .getInstance()
                .collection("artikel")
                .get()
                .addOnSuccessListener {
                    documents ->
                    for (document in documents){
                        val model = ArtikelModel()
                        model.judul_artikel = document.data["judul_artikel"].toString()
                        model.deskripsi = document.data["deskripsi_artikel"].toString()
                        model.image = document.data["image_artikel"].toString()
                        model.uid = document.data["uid"].toString()

                        listItems.add(model)
                    }
                    artikelList.postValue(listItems)
                }.addOnFailureListener {
                    exception -> Log.w(TAG, "Error:", exception)
                }
        }catch (error: Exception){
            error.printStackTrace()
        }
    }
    fun getArtikelList() : LiveData<ArrayList<ArtikelModel>>{
        return artikelList
    }
}