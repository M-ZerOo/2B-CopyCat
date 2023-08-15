package com.melfouly.bestbuycopycat.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.melfouly.bestbuycopycat.R
import com.melfouly.bestbuycopycat.databinding.FragmentFilterBottomSheetBinding
import com.melfouly.bestbuycopycat.domain.model.CategoryResponse
import com.melfouly.bestbuycopycat.domain.model.CategoryState
import com.melfouly.bestbuycopycat.domain.usecase.MealsUseCase
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
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

        dialog?.setOnShowListener {
            val bottomSheet = it as BottomSheetDialog
            val bottomSheetBehavior = bottomSheet.behavior
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.brandButton.setOnClickListener {
            if (binding.brandRadioGroup.visibility == View.VISIBLE) {
                binding.brandButton.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.black
                    )
                )
                binding.brandButton.icon = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.down_arrow
                )
                binding.brandButton.iconTint =
                    ContextCompat.getColorStateList(requireActivity(), R.color.black)
                binding.brandRadioGroup.visibility = View.GONE
            } else {
                binding.brandButton.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.orange
                    )
                )
                binding.brandButton.icon = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.arrow_up
                )
                binding.brandButton.iconTint =
                    ContextCompat.getColorStateList(requireActivity(), R.color.orange)
                binding.brandRadioGroup.visibility = View.VISIBLE
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

        binding.processButton.setOnClickListener {
            dismiss()
        }

        binding.resetButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }


}