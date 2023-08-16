package com.melfouly.bestbuycopycat.presentation

import android.content.Context
import android.content.SharedPreferences
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.melfouly.bestbuycopycat.R
import com.melfouly.bestbuycopycat.data.mapper.toProduct
import com.melfouly.bestbuycopycat.databinding.FragmentDealsBinding
import com.melfouly.bestbuycopycat.databinding.SortBottomSheetDialogBinding
import com.melfouly.bestbuycopycat.domain.model.CategoryResponse
import com.melfouly.bestbuycopycat.domain.model.State
import com.melfouly.bestbuycopycat.domain.model.MealResponse
import com.melfouly.bestbuycopycat.domain.model.Product
import com.melfouly.bestbuycopycat.domain.usecase.MealsUseCase
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DealsFragment : Fragment() {
    private lateinit var binding: FragmentDealsBinding

    @Inject
    lateinit var useCase: MealsUseCase
    private lateinit var adapter: DealsAdapter
    private lateinit var sortBottomSheetDialog: BottomSheetDialog
    private lateinit var sortBinding: SortBottomSheetDialogBinding
    private lateinit var productDisposable: Disposable
    private val _state = MutableStateFlow(State(isLoading = true))
    private val state: StateFlow<State> get() = _state
    private val bundle = Bundle()
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentDealsBinding.inflate(inflater)

        lifecycleScope.launch {
            state.collect {
                if (it.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                    chooseCategory()
                    checkDesignLayout()
                } else if (it.success != null) {
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireActivity(), "Error: ${it.error}", Toast.LENGTH_SHORT)
                        .show()
                }
                Log.d("TAG", "onCreateView: checkDesign called")
            }
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
                productDisposable = d
            }

            override fun onSuccess(t: CategoryResponse) {
                val list = arrayListOf<Product>()
                t.categories.forEach {
                    list.add(it.toProduct())
                }
                _state.value?.let {
                    it.isLoading = false
                    it.success = list
                }
                adapter.submitList(list)
                binding.recyclerView.adapter = adapter
                binding.progressBar.visibility = View.GONE
                val categoryNameList = arrayListOf<String>()
                t.categories.forEach {
                    categoryNameList.add(it.strCategory)
                }
                saveCategoryToBundle(categoryNameList)
            }

            override fun onError(e: Throwable) {
                _state.value?.let {
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
                adapter.submitList(state.value?.success)
                binding.recyclerView.adapter = adapter
            }

            "Grid" -> {
                val manager = LinearLayoutManager(requireActivity())
                manager.orientation = LinearLayoutManager.VERTICAL
                binding.recyclerView.layoutManager = manager
                adapter = DealsAdapter(0)
                adapter.submitList(state.value?.success)
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
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.show(parentFragmentManager, null)
    }

    private fun saveCategoryToBundle(categories: ArrayList<String>) {
        bundle.putStringArrayList("categories", categories)
    }

    private fun getMeals(categoryName: String) {
        val mealsObservables = useCase.getMeals(categoryName)
        mealsObservables.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(setMealsObserver())
    }

    private fun setMealsObserver(): SingleObserver<MealResponse> {
        return object : SingleObserver<MealResponse> {
            override fun onSubscribe(d: Disposable) {
                productDisposable = d
            }

            override fun onSuccess(t: MealResponse) {
                val list = arrayListOf<Product>()
                t.meals.forEach {
                    list.add(it.toProduct())
                }
                state.value?.let {
                    it.isLoading = false
                    it.success = list
                }
                adapter.submitList(list)
                binding.recyclerView.adapter = adapter
                binding.progressBar.visibility = View.GONE
            }

            override fun onError(e: Throwable) {
                _state.value?.let {
                    it.isLoading = false
                    it.error = e.message
                    Toast.makeText(requireActivity(), "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("TAG", "onError: mealsObserver: ${e.message}")
                }
            }
        }
    }

    private fun getCategoryName(id: Int): String {
        return when (id) {
            0 -> "Beef"
            1 -> "Chicken"
            2 -> "Dessert"
            3 -> "Lamb"
            4 -> "Miscellaneous"
            5 -> "Pasta"
            6 -> "Pork"
            7 -> "Seafood"
            8 -> "Side"
            9 -> "Starter"
            10 -> "Vegan"
            11 -> "Vegetarian"
            12 -> "Breakfast"
            else -> "Goat"
        }
    }

    private fun chooseCategory() {
        sharedPref = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val categoryCheckedId = sharedPref.getInt("radioButton", -1)
        Log.d("TAG", "categoryCheckedId: $categoryCheckedId")
        if (categoryCheckedId == -1) {
            getCategories()
            Log.d("TAG", "chooseCategory: getCategories called")
        } else {
            getMeals(getCategoryName(categoryCheckedId))
            Log.d("TAG", "chooseCategory: getMeals called")
        }
        checkDesignLayout()
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: called")
        chooseCategory()
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onPause: called")
    }

    override fun onStop() {
        super.onStop()
        productDisposable.dispose()
        sharedPref = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        sharedPref.edit().putInt("radioButton", -1).apply()
        val categoryCheckedId = sharedPref.getInt("radioButton", -1)
        Log.d("TAG", "categoryCheckedId: $categoryCheckedId")
    }


}