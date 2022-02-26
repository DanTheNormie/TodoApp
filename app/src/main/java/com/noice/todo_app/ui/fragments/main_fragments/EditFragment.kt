package com.noice.todo_app.ui.fragments.main_fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.noice.todo_app.R
import com.noice.todo_app.data.model.Subtask
import com.noice.todo_app.databinding.EditFragmentBinding
import com.noice.todo_app.ui.viewmodels.EditViewModel
import java.util.*


class EditFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = EditFragment()
    }

    private lateinit var viewModel: EditViewModel
    private lateinit var bind: EditFragmentBinding
    private lateinit var arrAdapter: ArrayAdapter<String>

    private var DummyTestListforSubtasks = arrayListOf(
        Subtask("1", "subtask #1", true),
        Subtask("2", "subtask #2", false),
        Subtask("3", "subtask #3", false)
    )
    private lateinit var subtasksAdapter: SubtasksRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDisableDragStyle)
        bind = EditFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[EditViewModel::class.java]
        subtasksAdapter = SubtasksRVAdapter(requireContext(), DummyTestListforSubtasks)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        configureSubtasksRecyclerView()

        configureListsMenu()

        configureDueDateAndTimeView()

        configureReminder()

        return bind.root
    }

    private fun configureReminder() {
        bind.reminderTil.setEndIconOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                context, { timePicker, selectedHour, selectedMinute ->
                    val amOrPm = if (selectedHour < 12) {
                        "AM"
                    } else {
                        "PM"
                    }
                    val hours = when {
                        selectedHour < 12 -> {
                            selectedHour
                        }
                        selectedHour == 12 -> {
                            12
                        }
                        else -> {
                            selectedHour % 12
                        }
                    }
                    val min = if (selectedMinute < 10) {
                        "0$selectedMinute"
                    } else {
                        "$selectedMinute"
                    }
                    bind.reminderEt.setText("$hours:$min $amOrPm")
                },
                hour,
                minute,
                false
            ) //Yes 24 hour time
            mTimePicker.show()
        }
    }

    private fun configureDueDateAndTimeView() {
        bind.dueTil.setEndIconOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd =
            DatePickerDialog(requireActivity(), { view, selectedyear, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                val resultDateTime = "$dayOfMonth/$monthOfYear/$selectedyear"

                bind.dueEt.setText(resultDateTime)

            }, year, month, day)

        dpd.show()
    }

    private fun configureListsMenu() {
        val lists = arrayOf(
            "Default",
            "Favourites",
            "Daily",
            "Weekly",
            "Monthly"
        )
        arrAdapter = ArrayAdapter(requireContext(), R.layout.menu_item_layout, lists)
    }

    private fun configureSubtasksRecyclerView() {
        bind.subtasksRv.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            adapter = subtasksAdapter

        }
        bind.subtasksEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                DummyTestListforSubtasks.add(
                    Subtask(
                        "${DummyTestListforSubtasks.size}",
                        bind.subtasksEt.text.toString()
                    )
                )
                subtasksAdapter.notifyItemInserted(DummyTestListforSubtasks.size - 1)
                val bottom: Int = bind.subtasksRv.adapter?.itemCount?.minus(1) ?: 0
                bind.subtasksRv.smoothScrollToPosition(bottom)
                bind.subtasksEt.setText("")
                return@setOnEditorActionListener true
            }
            false
        }

    }

    override fun onResume() {



        bind.listsDropdownMenu.setAdapter(arrAdapter)
        super.onResume()
    }

}