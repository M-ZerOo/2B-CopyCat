package com.melfouly.bestbuycopycat.presentation

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.melfouly.bestbuycopycat.R
import com.melfouly.bestbuycopycat.data.mapper.toProduct
import com.melfouly.bestbuycopycat.databinding.FragmentDealsBinding
import com.melfouly.bestbuycopycat.databinding.FragmentFilterBottomSheetBinding
import com.melfouly.bestbuycopycat.domain.model.MealResponse
import com.melfouly.bestbuycopycat.domain.model.Product
import com.melfouly.bestbuycopycat.domain.model.State
import com.melfouly.bestbuycopycat.domain.usecase.MealsUseCase
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment.
        binding = FragmentFilterBottomSheetBinding.inflate(inflater)

        val bundle = arguments?.getStringArrayList("categories")
        val sharedPref = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val buttonCheckedId = sharedPref.getInt("radioButton", -1)

        if (bundle != null) {
            for (i in bundle.indices) {
                val radioButton = RadioButton(requireActivity())
                radioButton.text = bundle[i]
                radioButton.id = i
                binding.categoryRadioGroup.addView(radioButton)
            }
            binding.categoryRadioGroup.check(buttonCheckedId)
        } else {
            Log.d("TAG", "onCreateView: bundle is null")
        }

        dialog?.setOnShowListener {
            val bottomSheet = it as BottomSheetDialog
            val bottomSheetBehavior = bottomSheet.behavior
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.categoryButton.setOnClickListener {
            if (binding.categoryRadioGroup.visibility == View.VISIBLE) {
                binding.categoryButton.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.black
                    )
                )
                binding.categoryButton.icon = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.down_arrow
                )
                binding.categoryButton.iconTint =
                    ContextCompat.getColorStateList(requireActivity(), R.color.black)
                binding.categoryRadioGroup.visibility = View.GONE
            } else {
                binding.categoryButton.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.orange
                    )
                )
                binding.categoryButton.icon = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.arrow_up
                )
                binding.categoryButton.iconTint =
                    ContextCompat.getColorStateList(requireActivity(), R.color.orange)
                binding.categoryRadioGroup.visibility = View.VISIBLE
            }
        }

        binding.colorButton.setOnClickListener {
            if (binding.colorRadioGroup.visibility == View.VISIBLE) {
                binding.colorButton.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.black
                    )
                )
                binding.colorButton.icon = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.down_arrow
                )
                binding.colorButton.iconTint =
                    ContextCompat.getColorStateList(requireActivity(), R.color.black)
                binding.colorRadioGroup.visibility = View.GONE
            } else {
                binding.colorButton.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.orange
                    )
                )
                binding.colorButton.icon = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.arrow_up
                )
                binding.colorButton.iconTint =
                    ContextCompat.getColorStateList(requireActivity(), R.color.orange)
                binding.colorRadioGroup.visibility = View.VISIBLE
            }
        }

        binding.categoryRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            Log.d("TAG", "onCreateView: ${i}")
            sharedPref.edit().putInt("radioButton", i).apply()

        }

        binding.processButton.setOnClickListener {
                dismiss()
        }

        binding.resetButton.setOnClickListener {
            sharedPref.edit().putInt("radioButton", -1).apply()
            dismiss()
        }

        return binding.root
    }


}