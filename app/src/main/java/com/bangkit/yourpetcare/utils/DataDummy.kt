package com.bangkit.yourpetcare.utils

import com.bangkit.yourpetcare.konsultasi.Dokter

object DataDummy {
    fun dummyDoctors() : ArrayList<Dokter> {
        val doctors = ArrayList<Dokter>()
        repeat(10) { i ->
            doctors.add(
                Dokter(
                    email = "drsloremipsum$i@gmail.com",
                    username = "Username $i",
                    nama = "Drs. loremipsum $i",
                    nomor = "0857111111$i",
                    riwayatPendidikan = "Universitas Lorem Ipsum $i",
                    tempatPraktik = "Bikini bottom",
                    uid = "112$i",
                    profesi = "Dokter Kucing"
                )
            )
        }
        return doctors
    }
}