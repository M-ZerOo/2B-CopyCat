package com.melfouly.bestbuycopycat.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.melfouly.bestbuycopycat.R
import com.melfouly.bestbuycopycat.databinding.FragmentDealsBinding
import com.melfouly.bestbuycopycat.databinding.SortBottomSheetDialogBinding
import com.melfouly.bestbuycopycat.domain.model.CategoryResponse
import com.melfouly.bestbuycopycat.domain.model.CategoryState
import com.melfouly.bestbuycopycat.domain.usecase.MealsUseCase
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class DealsFragment: Fragment() {
    private lateinit var binding: FragmentDealsBinding
    @Inject
    lateinit var useCase: MealsUseCase
    private lateinit var adapter: DealsAdapter
    private lateinit var sortBottomSheetDialog: BottomSheetDialog
    private lateinit var sortBinding: SortBottomSheetDialogBinding
    private lateinit var categoriesDisposable: Disposable
    private val _categoryState = MutableLiveData(CategoryState(isLoading = true))
    private val categoryState: LiveData<CategoryState> get() = _categoryState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentDealsBinding.inflate(inflater)

            getCategories()

        categoryState.observe(this.viewLifecycleOwner) {
            if(it.isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                checkDesignLayout()
            } else if (it.success != null) {
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), "Error: ${it.error}", Toast.LENGTH_SHORT).show()
            }
            Log.d("TAG", "onCreateView: checkDesign called")
        }

        setupBottomSheet()

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

        binding.filterButton.setOnClickListener {
            showFilterBottomSheet()
        }

        return binding.root
    }

    private fun getCategories() {
        val categoriesObservables = useCase.getCategories() // Create Observable
        categoriesObservables.subscribeOn(Schedulers.io()) // Upstream in IO thread
            .observeOn(AndroidSchedulers.mainThread()) // Downstream in Main thread
            .subscribe(setCategoriesObserver())
    }

    private fun setCategoriesObserver(): SingleObserver<CategoryResponse> {
        return object : SingleObserver<CategoryResponse> {
            override fun onSubscribe(d: Disposable) {
                categoriesDisposable = d
            }

            override fun onSuccess(t: CategoryResponse) {
                _categoryState.value?.let {
                    it.isLoading = false
                    it.success = t
                }
                adapter.submitList(t.categories)
                binding.recyclerView.adapter = adapter
                binding.progressBar.visibility = View.GONE

            }

            override fun onError(e: Throwable) {
                _categoryState.value?.let {
                    it.isLoading = false
                    it.error = e.message
                }
                Toast.makeText(requireActivity(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onError: categoriesObserver: ${e.message}")
            }
        }
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
                adapter.submitList(categoryState.value?.success?.categories)
                binding.recyclerView.adapter = adapter
            }

            "Grid" -> {
                val manager = LinearLayoutManager(requireActivity())
                manager.orientation = LinearLayoutManager.VERTICAL
                binding.recyclerView.layoutManager = manager
                adapter = DealsAdapter(0)
                adapter.submitList(categoryState.value?.success?.categories)
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

    private fun showFilterBottomSheet() {
        val bottomSheetFragment = FilterBottomSheetFragment()
        bottomSheetFragment.show(parentFragmentManager, null)
    }

    override fun onDestroy() {
        categoriesDisposable.dispose()
        super.onDestroy()
    }


}