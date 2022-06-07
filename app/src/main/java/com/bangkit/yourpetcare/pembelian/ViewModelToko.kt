package com.bangkit.yourpetcare.pembelian

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.yourpetcare.artikel.artikel.ArtikelModel
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class ViewModelToko : ViewModel() {

    private val produkList = MutableLiveData<ArrayList<TokoModel>>()
    private val listItems = ArrayList<TokoModel>()
    private val TAG = ViewModelToko::class.java.simpleName

    fun setListObat(){
        listItems.clear()

        try{
            FirebaseFirestore
                .getInstance()
                .collection("produk")
                .whereEqualTo("category", "Obat")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        val model = TokoModel()
                        model.namaProduk = document.data["nama_produk"].toString()
                        model.deskripsiProduk = document.data["deskripsi_produk"].toString()
                        model.hargaProduk = document.data["harga_produk"].toString().toLong()
                        model.imgProduk = document.data["image_produk"].toString()
                        model.produk_id = document.data["produk_id"].toString()
                        model.uid = document.data["uid"].toString()

                        listItems.add(model)
                    }
                    produkList.postValue(listItems)
                }.addOnFailureListener {
                        exception -> Log.w(TAG, "Error:", exception)
                }
        }catch (error: Exception){
            error.printStackTrace()
        }
    }
    fun setListPet(){
        listItems.clear()

        try{
            FirebaseFirestore
                .getInstance()
                .collection("produk")
                .whereEqualTo("category", "Petshop")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        val model = TokoModel()
                        model.namaProduk = document.data["nama_produk"].toString()
                        model.deskripsiProduk = document.data["deskripsi_produk"].toString()
                        model.hargaProduk = document.data["harga_produk"].toString().toLong()
                        model.imgProduk = document.data["image_produk"].toString()
                        model.produk_id = document.data["produk_id"].toString()
                        model.uid = document.data["uid"].toString()

                        listItems.add(model)
                    }
                    produkList.postValue(listItems)
                }.addOnFailureListener {
                        exception -> Log.w(TAG, "Error:", exception)
                }
        }catch (error: Exception){
            error.printStackTrace()
        }
    }
    fun getProdukList() : LiveData<ArrayList<TokoModel>> {
        return produkList
    }
}