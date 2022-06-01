package com.bangkit.yourpetcare.beranda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bangkit.yourpetcare.*
import com.bangkit.yourpetcare.artikel.ArtikelFragment
import com.bangkit.yourpetcare.databinding.ActivityMainBinding
import com.bangkit.yourpetcare.konsultasi.KonsultasiFragment
import com.bangkit.yourpetcare.pembelian.TokoFragment
import com.bangkit.yourpetcare.scan.ScanFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val nav = findViewById<BottomNavigationView>(R.id.navigation_view)
        nav.setOnNavigationItemSelectedListener { item: MenuItem ->
            var selectedItemFragment : Fragment? = TokoFragment()
            when(item.itemId){
                R.id.fragment_konsultasi -> {
                    nav.menu.findItem(R.id.fragment_konsultasi).isEnabled = false
                    nav.menu.findItem(R.id.fragment_scan).isEnabled = true
                    nav.menu.findItem(R.id.fragment_artikel).isEnabled = true
                    nav.menu.findItem(R.id.fragment_toko).isEnabled = true
                    selectedItemFragment = KonsultasiFragment()
                }
                R.id.fragment_scan -> {
                    nav.menu.findItem(R.id.fragment_konsultasi).isEnabled = true
                    nav.menu.findItem(R.id.fragment_scan).isEnabled = false
                    nav.menu.findItem(R.id.fragment_artikel).isEnabled = true
                    nav.menu.findItem(R.id.fragment_toko).isEnabled = true
                    selectedItemFragment = ScanFragment()
                }
                R.id.fragment_artikel -> {
                    nav.menu.findItem(R.id.fragment_konsultasi).isEnabled = true
                    nav.menu.findItem(R.id.fragment_scan).isEnabled = true
                    nav.menu.findItem(R.id.fragment_artikel).isEnabled = false
                    nav.menu.findItem(R.id.fragment_toko).isEnabled = true
                    selectedItemFragment = ArtikelFragment()
                }
                R.id.fragment_toko -> {
                    nav.menu.findItem(R.id.fragment_konsultasi).isEnabled = true
                    nav.menu.findItem(R.id.fragment_scan).isEnabled = true
                    nav.menu.findItem(R.id.fragment_artikel).isEnabled = true
                    nav.menu.findItem(R.id.fragment_toko).isEnabled = false
                    selectedItemFragment = TokoFragment()
                }
            }
            val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
            if (selectedItemFragment != null){
                transaction.replace(R.id.container, selectedItemFragment)
            }
            transaction.commit()
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}