package com.melfouly.bestbuycopycat.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.melfouly.bestbuycopycat.R
import com.melfouly.bestbuycopycat.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment.
        binding = FragmentNotificationsBinding.inflate(inflater)

        binding.dateOfBirth.setOnClickListener {
            onClickDatePicker(binding.dateOfBirth)
        }

        return binding.root
    }

    private fun onClickDatePicker(editText: EditText) {
        val dobCalendar = Calendar.getInstance()
        val selectedDate = editText.text.toString()
        if (selectedDate.isNotBlank()) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val d = sdf.parse(selectedDate)
            dobCalendar.time = d
        }
        val selectedYear = dobCalendar.get(Calendar.YEAR)
        val selectedMonth = dobCalendar.get(Calendar.MONTH)
        val selectedDay = dobCalendar.get(Calendar.DAY_OF_MONTH)

        val dateOfBirth = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            dobCalendar.set(Calendar.YEAR, year)
            dobCalendar.set(Calendar.MONTH, month)
            dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dob = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            editText.setText(dob.format(dobCalendar.time))
        }

        val datePickerDialog = DatePickerDialog(requireActivity(), com.google.android.material.R.style.AlertDialog_AppCompat_Light, dateOfBirth, selectedYear, selectedMonth, selectedDay)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }


}