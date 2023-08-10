package com.melfouly.bestbuycopycat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {

    private val bottomBarItemCount = 6

    override fun getItemCount(): Int = bottomBarItemCount

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 ->CategoriesFragment()
            2 ->DealsFragment()
            3 ->NotificationsFragment()
            4 ->AccountFragment()
            else -> SearchFragment()
        }
    }
}