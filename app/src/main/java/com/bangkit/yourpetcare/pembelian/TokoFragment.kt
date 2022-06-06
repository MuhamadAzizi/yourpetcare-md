package com.bangkit.yourpetcare.pembelian

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.yourpetcare.databinding.FragmentTokoBinding
import com.bangkit.yourpetcare.pembelian.keranjang.KeranjangActivity
import com.google.android.material.tabs.TabLayout

class TokoFragment : Fragment() {

    private var binding : FragmentTokoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokoBinding.inflate(inflater, container, false)
        binding?.tabs?.addTab(binding?.tabs?.newTab()!!.setText("Pemesanan"))
        binding?.tabs?.addTab(binding?.tabs?.newTab()!!.setText("Petshop"))
        binding?.tabs?.addTab(binding?.tabs?.newTab()!!.setText("Obat"))



        val adapter = ViewPagerAdapter(activity, childFragmentManager, binding?.tabs!!.tabCount)
        binding?.viewPager?.adapter = adapter
        binding?.viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding?.tabs))
        binding?.tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding?.viewPager?.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding?.keranjang?.setOnClickListener {
            startActivity(Intent(requireContext(), KeranjangActivity::class.java))
        }
        return binding?.root
    }





        override fun onDestroy() {
            super.onDestroy()
            binding = null
        }

}