package com.melfouly.bestbuycopycat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.melfouly.bestbuycopycat.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentAccountBinding.inflate(layoutInflater)

        binding.changeLanguageButton.setOnClickListener {
            LocaleHelper.changeLocale(requireActivity())
            requireActivity().recreate()
        }

        return binding.root
    }


}