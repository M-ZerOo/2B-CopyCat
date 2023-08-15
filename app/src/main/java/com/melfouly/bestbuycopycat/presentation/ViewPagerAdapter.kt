package com.melfouly.bestbuycopycat.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.melfouly.bestbuycopycat.presentation.AccountFragment
import com.melfouly.bestbuycopycat.presentation.CategoriesFragment
import com.melfouly.bestbuycopycat.presentation.DealsFragment
import com.melfouly.bestbuycopycat.presentation.HomeFragment
import com.melfouly.bestbuycopycat.presentation.NotificationsFragment
import com.melfouly.bestbuycopycat.presentation.SearchFragment
import javax.inject.Inject

class ViewPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {

    private val bottomBarItemCount = 6

    override fun getItemCount(): Int = bottomBarItemCount

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> CategoriesFragment()
            2 -> DealsFragment()
            3 -> NotificationsFragment()
            4 -> AccountFragment()
            else -> SearchFragment()
        }
    }
}