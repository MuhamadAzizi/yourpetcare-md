package com.bangkit.yourpetcare.pembelian

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bangkit.yourpetcare.pembelian.obat.ObatFragment
import com.bangkit.yourpetcare.pembelian.pesanan.PesananFragment
import com.bangkit.yourpetcare.pembelian.shop.ShopFragment

class ViewPagerAdapter(var context:FragmentActivity?, fm:FragmentManager, var totalTabs:Int)
    :FragmentPagerAdapter(fm){

        override fun getCount(): Int {
            return totalTabs
        }

        override fun getItem(position: Int): Fragment {
            return when(position){
                0->{
                    PesananFragment()
                }
                1->{
                    ShopFragment()
                }
                2->{
                    ObatFragment()
                }
                else->getItem(position)
            }
        }


}