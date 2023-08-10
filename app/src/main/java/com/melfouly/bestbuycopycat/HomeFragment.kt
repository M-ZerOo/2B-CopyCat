package com.melfouly.bestbuycopycat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.melfouly.bestbuycopycat.databinding.ActivityMainBinding
import com.melfouly.bestbuycopycat.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager: ViewPager2
//    private lateinit var adapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentHomeBinding.inflate(inflater)
        viewPager = requireActivity().findViewById(R.id.viewPager)

        binding.searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                viewPager.currentItem = 5
//                navigateToSearchFragment()
            }
        }
        binding.searchView.setOnClickListener {
            viewPager.currentItem = 5
//            navigateToSearchFragment()
        }

        return binding.root
    }

    private fun navigateToSearchFragment() {
        val searchFragment = SearchFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.container, searchFragment)
            .addToBackStack(null)
            .commit()
    }


}