package com.projectgithub.presentation.detail.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    private val bundle: Bundle,
    private val fragmentTabs: ArrayList<Fragment>,
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragmentTabs.size

    override fun createFragment(position: Int): Fragment {
        fragmentTabs[position].arguments = bundle
        return fragmentTabs[position]
    }

}