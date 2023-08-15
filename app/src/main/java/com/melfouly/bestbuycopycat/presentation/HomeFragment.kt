package com.melfouly.bestbuycopycat.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.melfouly.bestbuycopycat.R
import com.melfouly.bestbuycopycat.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OffersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentHomeBinding.inflate(inflater)
        viewPager = requireActivity().findViewById(R.id.viewPager)

        val laptopsList = listOf<DataItem>(
            DataItem.HeaderItem(1, "Laptop #1", "5000 EGP"),
            DataItem.ChildItem(2, "Laptop #2", "2000 EGP"),
            DataItem.ChildItem(3, "Laptop #3", "3000 EGP"),
            DataItem.ChildItem(4, "Laptop #4", "4000 EGP"),
            DataItem.ChildItem(5, "Laptop #5", "4200 EGP"),
            DataItem.ChildItem(6, "Laptop #6", "9000 EGP")
        )

        val manager = GridLayoutManager(requireActivity(), 2)
        manager.orientation = GridLayoutManager.HORIZONTAL
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            // This position determined based on the item position in your list.
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 2
                else -> 1
            }
        }
        binding.laptopsRecyclerView.layoutManager = manager
        adapter = OffersAdapter()
        adapter.submitList(laptopsList)
        binding.laptopsRecyclerView.adapter = adapter

        binding.searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                viewPager.currentItem = 5
            }
        }
        binding.searchView.setOnClickListener {
            viewPager.currentItem = 5
        }

        return binding.root
    }


}