package com.melfouly.bestbuycopycat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.melfouly.bestbuycopycat.databinding.FragmentDealsBinding
import com.melfouly.bestbuycopycat.databinding.SortBottomSheetDialogBinding
import com.melfouly.bestbuycopycat.model.Product

class DealsFragment : Fragment() {

    private lateinit var binding: FragmentDealsBinding
    private lateinit var adapter: DealsAdapter
    private lateinit var sortBottomSheetDialog: BottomSheetDialog
    private lateinit var sortBinding: SortBottomSheetDialogBinding

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

        setupBottomSheet()
        checkDesignLayout()

        binding.designLayoutButton.setOnClickListener {
            changeDesignLayout()
        }

        binding.sortButton.setOnClickListener {
            sortBottomSheetDialog.show()
            sortBottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            sortBinding.positionLTH.setOnClickListener { checkBox1Pressed() }
            sortBinding.positionHTL.setOnClickListener { checkBox2Pressed() }
            sortBinding.priceLTH.setOnClickListener { checkBox3Pressed() }
            sortBinding.priceHTL.setOnClickListener { checkBox4Pressed() }
            sortBinding.quantityLTH.setOnClickListener { checkBox5Pressed() }
            sortBinding.quantityHTL.setOnClickListener { checkBox6Pressed() }
        }

        return binding.root
    }

    private fun setupBottomSheet() {
        sortBinding = SortBottomSheetDialogBinding.inflate(layoutInflater)
        sortBottomSheetDialog = BottomSheetDialog(requireActivity())
        sortBottomSheetDialog.setContentView(sortBinding.root)
    }

    private fun checkDesignLayout() {
        when (binding.designLayoutButton.text) {
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

    private fun changeDesignLayout() {
        if (binding.designLayoutButton.text == "List") {
            binding.designLayoutButton.text = "Grid"
            binding.designLayoutButton.icon =
                ContextCompat.getDrawable(requireActivity(), R.drawable.categories_icon)
            checkDesignLayout()
        } else {
            binding.designLayoutButton.text = "List"
            binding.designLayoutButton.icon =
                ContextCompat.getDrawable(requireActivity(), R.drawable.row_icon)
            checkDesignLayout()
        }
    }

    private fun checkBox1Pressed() {
        sortBinding.positionLTH.setTextColor(requireActivity().getColor(R.color.orange))
        sortBinding.positionHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.checkbox1.visibility = View.VISIBLE
        sortBinding.checkbox2.visibility = View.INVISIBLE
        sortBinding.checkbox3.visibility = View.INVISIBLE
        sortBinding.checkbox4.visibility = View.INVISIBLE
        sortBinding.checkbox5.visibility = View.INVISIBLE
        sortBinding.checkbox6.visibility = View.INVISIBLE
        sortBottomSheetDialog.hide()
    }

    private fun checkBox2Pressed() {
        sortBinding.positionLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.positionHTL.setTextColor(requireActivity().getColor(R.color.orange))
        sortBinding.priceLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.checkbox1.visibility = View.INVISIBLE
        sortBinding.checkbox2.visibility = View.VISIBLE
        sortBinding.checkbox3.visibility = View.INVISIBLE
        sortBinding.checkbox4.visibility = View.INVISIBLE
        sortBinding.checkbox5.visibility = View.INVISIBLE
        sortBinding.checkbox6.visibility = View.INVISIBLE
        sortBottomSheetDialog.hide()
    }

    private fun checkBox3Pressed() {
        sortBinding.positionLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.positionHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceLTH.setTextColor(requireActivity().getColor(R.color.orange))
        sortBinding.priceHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.checkbox1.visibility = View.INVISIBLE
        sortBinding.checkbox2.visibility = View.INVISIBLE
        sortBinding.checkbox3.visibility = View.VISIBLE
        sortBinding.checkbox4.visibility = View.INVISIBLE
        sortBinding.checkbox5.visibility = View.INVISIBLE
        sortBinding.checkbox6.visibility = View.INVISIBLE
        sortBottomSheetDialog.hide()
    }

    private fun checkBox4Pressed() {
        sortBinding.positionLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.positionHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceHTL.setTextColor(requireActivity().getColor(R.color.orange))
        sortBinding.quantityLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.checkbox1.visibility = View.INVISIBLE
        sortBinding.checkbox2.visibility = View.INVISIBLE
        sortBinding.checkbox3.visibility = View.INVISIBLE
        sortBinding.checkbox4.visibility = View.VISIBLE
        sortBinding.checkbox5.visibility = View.INVISIBLE
        sortBinding.checkbox6.visibility = View.INVISIBLE
        sortBottomSheetDialog.hide()
    }

    private fun checkBox5Pressed() {
        sortBinding.positionLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.positionHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityLTH.setTextColor(requireActivity().getColor(R.color.orange))
        sortBinding.quantityHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.checkbox1.visibility = View.INVISIBLE
        sortBinding.checkbox2.visibility = View.INVISIBLE
        sortBinding.checkbox3.visibility = View.INVISIBLE
        sortBinding.checkbox4.visibility = View.INVISIBLE
        sortBinding.checkbox5.visibility = View.VISIBLE
        sortBinding.checkbox6.visibility = View.INVISIBLE
        sortBottomSheetDialog.hide()
    }

    private fun checkBox6Pressed() {
        sortBinding.positionLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.positionHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.priceHTL.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityLTH.setTextColor(requireActivity().getColor(R.color.black))
        sortBinding.quantityHTL.setTextColor(requireActivity().getColor(R.color.orange))
        sortBinding.checkbox1.visibility = View.INVISIBLE
        sortBinding.checkbox2.visibility = View.INVISIBLE
        sortBinding.checkbox3.visibility = View.INVISIBLE
        sortBinding.checkbox4.visibility = View.INVISIBLE
        sortBinding.checkbox5.visibility = View.INVISIBLE
        sortBinding.checkbox6.visibility = View.VISIBLE
        sortBottomSheetDialog.hide()
    }


}