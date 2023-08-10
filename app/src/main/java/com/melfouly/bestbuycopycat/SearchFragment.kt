package com.melfouly.bestbuycopycat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.melfouly.bestbuycopycat.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentSearchBinding.inflate(inflater)
        viewPager = requireActivity().findViewById(R.id.viewPager)

        binding.backButton.setOnClickListener {
            viewPager.currentItem = 0
        }

        return binding.root
    }


}