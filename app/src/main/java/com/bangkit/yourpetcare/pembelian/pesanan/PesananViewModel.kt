package com.bangkit.yourpetcare.pembelian.pesanan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class PesananViewModel : ViewModel() {
    private val orderList = MutableLiveData<ArrayList<OrderModel>>()
    private val listItems = ArrayList<OrderModel>()
    private val TAG = PesananViewModel::class.java.simpleName

    fun setPemesananAll(){
        listItems.clear()

        try {
            FirebaseFirestore
                .getInstance()
                .collection("order")
                .get()
                .addOnSuccessListener {
                    document -> for (i in document){
                        val model = OrderModel()
                        model.date = i.data["date"].toString()
                        model.totalPrice = i.data["totalPrice"] as Long
                        model.username = i.data["username"].toString()
                        model.img = i.data["image"].toString()
                        model.orderId = i.data["orderId"].toString()
                        model.userId = i.data["userId"].toString()
                        model.id_produk = i.toObject(OrderModel::class.java).id_produk
                        listItems.add(model)
                    }
                    orderList.postValue(listItems)
                }.addOnFailureListener {
                        exception -> Log.w(TAG, "error", exception)
        }

        }catch (error : Exception){
            error.printStackTrace()
        }
    }

    fun setPemesananBelumBayarId(userId : String){
        listItems.clear()

        try {
            FirebaseFirestore
                .getInstance()
                .collection("order")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener {
                        document -> for (i in document){
                    val model = OrderModel()
                    model.date = i.data["date"].toString()
                    model.totalPrice = i.data["totalPrice"] as Long
                    model.username = i.data["username"].toString()
                    model.img = i.data["image"].toString()
                    model.orderId = i.data["orderId"].toString()
                    model.userId = i.data["userId"].toString()
                    model.id_produk = i.toObject(OrderModel::class.java).id_produk
                    listItems.add(model)
                }
                    orderList.postValue(listItems)
                }.addOnFailureListener {
                        exception -> Log.w(TAG, "error", exception)
                }

        }catch (error : Exception){
            error.printStackTrace()
        }
    }

    fun getOrderList() : LiveData<ArrayList<OrderModel>>{
        return orderList
    }
}