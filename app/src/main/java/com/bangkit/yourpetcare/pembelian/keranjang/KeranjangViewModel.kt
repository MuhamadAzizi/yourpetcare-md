package com.bangkit.yourpetcare.pembelian.keranjang

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class KeranjangViewModel : ViewModel() {

    private val cartList = MutableLiveData<ArrayList<KeranjangModel>>()
    private val listItems = ArrayList<KeranjangModel>()
    private val TAG = KeranjangModel::class.java.simpleName

    fun setListCart(){
        listItems.clear()

        try{
            FirebaseFirestore
                .getInstance()
                .collection("cart")
                .get()
                .addOnSuccessListener {
                    documents ->
                    for(document in documents){
                        val model = KeranjangModel()
                        model.userId = document.data["userId"].toString()
                        model.image = document.data["img"].toString()
                        model.name = document.data["nama_produk"].toString()
                        model.deskripsi = document.data["description_produk"].toString()
                        model.price = document.data["price"] as Long
                        model.qty = document.data["qty"] as Long
                        model.cartId = document.data["cartId"].toString()
//                        model.produkId = document.data["id_produk"].toString()

                        listItems.add(model)
                    }
                    cartList.postValue(listItems)
                }
                .addOnFailureListener {
                    exception -> Log.e(TAG, "Error getting document", exception)
                }
        }catch (error : Exception){
            error.printStackTrace()
        }
    }

    fun getCartList() : LiveData<ArrayList<KeranjangModel>>{
        return cartList
    }
}