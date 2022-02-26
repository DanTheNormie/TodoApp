package com.noice.todo_app.ui.fragments.main_fragments

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.noice.todo_app.R
import com.noice.todo_app.data.model.Subtask
import com.noice.todo_app.data.model.Task
import com.noice.todo_app.databinding.SubtasksItemLayoutBinding

class SubtasksRVAdapter(
    val context: Context,
    val startIconClickListener:(position: Int)->Unit,
    val endIconClickListener:(position:Int)->Unit,
    val onDoneClickListener:(position:Int,text:String)->Unit
): ListAdapter<Subtask, SubtasksRVAdapter.SubtasksViewHolder>(SubtasksDiffCallback()) {

    inner class SubtasksViewHolder(val bind: SubtasksItemLayoutBinding):RecyclerView.ViewHolder(bind.root){

        fun bind(position: Int){
            val subtask = currentList[position]
            var list = currentList



            bind.subtasksEt.setText(subtask.title)


            if (subtask.isCompleted){
                bind.subtasksTil.startIconDrawable = ContextCompat.getDrawable(context,R.drawable.ic_baseline_checked_circle_24)
                bind.subtasksEt.paintFlags = bind.subtasksEt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                bind.subtasksTil.startIconDrawable = ContextCompat.getDrawable(context,R.drawable.ic_baseline_unchecked_circle_24)
                bind.subtasksEt.paintFlags = bind.subtasksEt.paintFlags or -Paint.STRIKE_THRU_TEXT_FLAG
            }

            bind.subtasksTil.setStartIconOnClickListener {
                startIconClickListener.invoke(position)
            }
            bind.subtasksTil.setEndIconOnClickListener {
                endIconClickListener.invoke(position)
            }
            bind.subtasksEt.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    onDoneClickListener.invoke(position,bind.subtasksEt.text.toString())
                    return@setOnEditorActionListener true
                }

                false
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtasksRVAdapter.SubtasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subtasks_item_layout,parent,false)
        val bind = SubtasksItemLayoutBinding.bind(view)

       return SubtasksViewHolder(bind)
    }

    override fun onBindViewHolder(holder: SubtasksRVAdapter.SubtasksViewHolder, position: Int) {
        holder.bind(position)
    }
}

class SubtasksDiffCallback():DiffUtil.ItemCallback<Subtask>(){
    override fun areItemsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
        return oldItem == newItem
    }
}