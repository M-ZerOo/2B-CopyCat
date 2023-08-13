package com.melfouly.bestbuycopycat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.melfouly.bestbuycopycat.databinding.FragmentDealsBinding
import com.melfouly.bestbuycopycat.model.Product

class DealsFragment : Fragment() {

    private lateinit var binding: FragmentDealsBinding
    private lateinit var adapter: DealsAdapter
    private val dealsList = listOf<Product>(
        Product("Gift Card EGP100", "5.0", "EGP100.00", R.drawable.child_laptop),
        Product("Gift Card EGP300", "5.0", "EGP300.00", R.drawable.child_laptop),
        Product("Gift Card EGP500", "5.0", "EGP500.00", R.drawable.child_laptop),
        Product("Gift Card EGP1000", "4.9", "EGP1000.00", R.drawable.child_laptop)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentDealsBinding.inflate(inflater)

        checkDesignLayout()

        binding.designLayoutButton.setOnClickListener {
            if (binding.designLayoutButton.text == "List") {
                binding.designLayoutButton.text = "Grid"
                binding.designLayoutButton.icon = requireActivity().getDrawable(R.drawable.categories_icon)
                checkDesignLayout()
            } else {
                binding.designLayoutButton.text = "List"
                binding.designLayoutButton.icon = requireActivity().getDrawable(R.drawable.row_icon)
                checkDesignLayout()
            }
        }

        return binding.root
    }

    private fun checkDesignLayout() {
        when(binding.designLayoutButton.text) {
            "List" -> {
                val manager = GridLayoutManager(requireActivity(), 2)
                manager.orientation = GridLayoutManager.VERTICAL
                binding.recyclerView.layoutManager = manager
                adapter = DealsAdapter(1)
                adapter.submitList(dealsList)
                binding.recyclerView.adapter = adapter
            }
            "Grid" -> {
                val manager = LinearLayoutManager(requireActivity())
                manager.orientation = LinearLayoutManager.VERTICAL
                binding.recyclerView.layoutManager = manager
                adapter = DealsAdapter(0)
                adapter.submitList(dealsList)
                binding.recyclerView.adapter = adapter
            }
        }
    }

}