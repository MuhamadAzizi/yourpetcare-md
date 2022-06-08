package com.bangkit.yourpetcare.konsultasi.detail_dokter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.yourpetcare.R
import com.bangkit.yourpetcare.konsultasi.Dokter

class DetailDokterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dokter)

        val getData = intent.getParcelableExtra<Dokter>(EXTRA_DATA)
        Log.d(TAG, "onCreate: $getData")
    }

    companion object {
        const val EXTRA_DATA = "detail_dokter_extra_data"
        val TAG = DetailDokterActivity::class.java.simpleName
    }
}