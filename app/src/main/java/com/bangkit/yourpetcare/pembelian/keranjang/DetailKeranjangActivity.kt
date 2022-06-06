package com.bangkit.yourpetcare.pembelian.keranjang

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.databinding.ActivityDetailKeranjangBinding
import com.bangkit.yourpetcare.databinding.FragmentObatBinding
import com.bangkit.yourpetcare.pembelian.DetailPembelianActivity
import com.bangkit.yourpetcare.pembelian.TokoFragment
import com.bangkit.yourpetcare.pembelian.obat.ObatFragment
import com.bangkit.yourpetcare.pembelian.pesanan.PesananFragment
import com.bangkit.yourpetcare.pembelian.shop.ShopFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class DetailKeranjangActivity : AppCompatActivity() {

    private var adapter : KeranjangAdapter? = null
    private var formater : NumberFormat = DecimalFormat("#,###")
    private var cartList : ArrayList<KeranjangModel> = ArrayList()
    private var username : String? = null
    private var binding : ActivityDetailKeranjangBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKeranjangBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.imageButtondetailpesanan?.setOnClickListener {
            onBackPressed()
        }

        binding?.addProdukDetail?.setOnClickListener {
            val obatFragment = TokoFragment()
            val fragment : Fragment? =

            supportFragmentManager.findFragmentByTag(ObatFragment::class.java.simpleName)

            if(fragment !is ObatFragment){
                supportFragmentManager.beginTransaction()
                    .add(R.id.constraint2, obatFragment, TokoFragment::class.java.simpleName)
                    .commit()

                binding?.imageButtondetailpesanan?.visibility = View.GONE
                binding?.addProdukDetail?.visibility = View.GONE
                binding?.textView8?.visibility = View.GONE
                binding?.recyclerviewDetailPesanan?.visibility = View.GONE
                binding?.view4?.visibility = View.GONE
                binding?.textView9?.visibility = View.GONE
                binding?.textView10?.visibility = View.GONE
                binding?.textView11?.visibility = View.GONE
                binding?.textView12?.visibility = View.GONE
                binding?.textView13?.visibility = View.GONE
                binding?.view5?.visibility = View.GONE
                binding?.textView14?.visibility = View.GONE
                binding?.textView15?.visibility = View.GONE
                binding?.textView16?.visibility = View.GONE
                binding?.textView17?.visibility = View.GONE
                binding?.textView18?.visibility = View.GONE
                binding?.radioGroup?.visibility = View.GONE
                binding?.view20?.visibility = View.GONE
                binding?.textView21?.visibility = View.GONE
                binding?.tvTotal?.visibility = View.GONE
                binding?.checkout?.visibility = View.GONE
            }
        }

        initCarList()

        binding?.checkout?.setOnClickListener {
            checkOutProduct()
        }
    }

    private fun initCarList() {
        binding?.recyclerviewDetailPesanan?.layoutManager = LinearLayoutManager(this)
        adapter = KeranjangAdapter(null, null, "checkout")
        binding?.recyclerviewDetailPesanan?.adapter = adapter

        val view = ViewModelProvider(this)[KeranjangViewModel::class.java]
        binding?.progressbardetailpesanan?.visibility = View.VISIBLE
        view.setListCart()
        view.getCartList().observe(this){
            cart -> if(cart.size > 0){
                binding?.progressbardetailpesanan?.visibility = View.GONE
                binding?.checkout?.visibility = View.VISIBLE
                adapter!!.setData(cart)
                setSubTotal()
            }else{
                binding?.progressbardetailpesanan?.visibility = View.GONE
            }
        }
    }

    private fun setSubTotal() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var totalPrice = 0L
        FirebaseFirestore
            .getInstance()
            .collection("cart")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener {
                document -> for(i in document){
                    totalPrice = totalPrice.plus(i.data["price"] as Long)
                }
                binding?.tvTotal?.text = "Rp. ${formater.format(totalPrice)}"
                binding?.textView14?.text = "Rp. ${formater.format(totalPrice)}"
            }
    }

    private fun checkOutProduct() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Harap ditunggu hingga proses selesai")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        cartList.clear()

        var totalPrice = 0L
        var uid = FirebaseAuth.getInstance().currentUser!!.uid

        getUserId(uid)

        FirebaseFirestore
            .getInstance()
            .collection("cart")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener {
                documents -> for (i in documents){
                    val model = KeranjangModel()
                    model.cartId = i.data["cartId"].toString()
//                    model.produkId = i.data["id_produk"].toString()
                    model.image = i.data["img"].toString()
                    model.name = i.data["nama_produk"].toString()
                    model.qty = i.data["qty"].toString().toLong()
                    model.price = i.data["price"] as Long
                    model.deskripsi = i.data["description_produk"].toString()
                    model.userId = i.data["userId"].toString()

                totalPrice = totalPrice.plus(model.price!!)
                cartList.add(model)
                }

                Timer().schedule(2000){
                    val orderId = System.currentTimeMillis().toString()
                    val df = SimpleDateFormat("dd MM yyyy, hh:mm:ss")
                    val formatDate : String = df.format(Date())

                    val data = mapOf(
                        "orderId" to orderId,
                        "userId" to uid,
                        "id_produk" to cartList,
//                        "img" to cartList[0].image,
                        "date" to formatDate,
                        "status" to "Belum Bayar",
                        "totalPrice" to totalPrice,
                        "ongkir" to 0L,
                        "paymentProof" to "",
                        "username" to username
                    )
                    FirebaseFirestore
                        .getInstance()
                        .collection("order")
                        .document(orderId)
                        .set(data)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                deleteCart(progressDialog)
                            }else{
                                progressDialog.dismiss()
                                showFailureDialogCart()
                            }
                        }
                }
            }
    }

    private fun deleteCart(progressDialog: ProgressDialog) {
            for (i in cartList.indices){
                cartList[i].cartId?.let {
                    FirebaseFirestore
                        .getInstance()
                        .collection("cart")
                        .document(it)
                        .delete()
                }
            }
        progressDialog.dismiss()
        showSuccessDialogCart()

    }

    private fun showSuccessDialogCart() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Membuat Pesanan")
            .setMessage("Anda berhasil membuat pesanan, cek pada halaman pesanan untuk lebih detailnya")
            .setIcon(R.drawable.ic_baseline_check_24)
            .setPositiveButton("oke") { dialog, _ ->
                dialog.dismiss()
                binding?.tvTotal?.text = "Rp. 0"
                binding?.textView14?.text = "Rp. 0"
                binding?.checkout?.visibility = View.GONE
            }.show()
        redirectPesanan()
    }

    private fun redirectPesanan() {
        val pesananFragment = TokoFragment()
        val fragment: Fragment? =

            supportFragmentManager.findFragmentByTag(TokoFragment::class.java.simpleName)

        if (fragment !is ObatFragment) {
            supportFragmentManager.beginTransaction()
                .add(R.id.constraint2, pesananFragment, TokoFragment::class.java.simpleName)
                .commit()

            binding?.imageButtondetailpesanan?.visibility = View.GONE
            binding?.addProdukDetail?.visibility = View.GONE
            binding?.textView8?.visibility = View.GONE
            binding?.recyclerviewDetailPesanan?.visibility = View.GONE
            binding?.view4?.visibility = View.GONE
            binding?.textView9?.visibility = View.GONE
            binding?.textView10?.visibility = View.GONE
            binding?.textView11?.visibility = View.GONE
            binding?.textView12?.visibility = View.GONE
            binding?.textView13?.visibility = View.GONE
            binding?.view5?.visibility = View.GONE
            binding?.textView14?.visibility = View.GONE
            binding?.textView15?.visibility = View.GONE
            binding?.textView16?.visibility = View.GONE
            binding?.textView17?.visibility = View.GONE
            binding?.textView18?.visibility = View.GONE
            binding?.radioGroup?.visibility = View.GONE
            binding?.view20?.visibility = View.GONE
            binding?.textView21?.visibility = View.GONE
            binding?.tvTotal?.visibility = View.GONE
            binding?.checkout?.visibility = View.GONE
        }
    }

    private fun showFailureDialogCart() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Membuat Pesanan")
            .setMessage("Anda gagal membuat pesanan, cek koneksi internet anda")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("oke") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun getUserId(uid: String) {
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                username = ""+it.data?.get("username")
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}