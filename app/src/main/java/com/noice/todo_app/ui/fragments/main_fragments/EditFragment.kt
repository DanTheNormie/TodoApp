package com.noice.todo_app.ui.fragments.main_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.noice.todo_app.R
import com.noice.todo_app.databinding.EditFragmentBinding
import com.noice.todo_app.ui.viewmodels.EditViewModel

class EditFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = EditFragment()
    }

    private lateinit var viewModel: EditViewModel
    private lateinit var bind: EditFragmentBinding
    private lateinit var arrAdapter:ArrayAdapter<String>

    private var DummyTestListforSubtasks = mutableListOf(
        Pair(1,"list 1"),
        Pair(2,"list 2"),
        Pair(3,"list 3")
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        bind = EditFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[EditViewModel::class.java]
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        configureSubtasksRecyclerView()



        val lists = arrayOf(
            "Default",
            "Favourites",
            "Daily",
            "Weekly",
            "Monthly"
        )
        arrAdapter = ArrayAdapter(requireContext(), R.layout.menu_item_layout,lists)

        return bind.root
    }

    private fun configureSubtasksRecyclerView() {
        bind.subtasksRv.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            adapter = null
        }
        bind.subtasksEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                
            }
            false
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {



        bind.listsDropdownMenu.setAdapter(arrAdapter)
        super.onResume()
    }

}