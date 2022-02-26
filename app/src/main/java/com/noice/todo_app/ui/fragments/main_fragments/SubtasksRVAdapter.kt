package com.noice.todo_app.ui.fragments.main_fragments

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.noice.todo_app.R
import com.noice.todo_app.data.model.Subtask
import com.noice.todo_app.databinding.SubtasksItemLayoutBinding

class SubtasksRVAdapter(
    val context: Context,
    val subtasksList: ArrayList<Subtask>
) : RecyclerView.Adapter<SubtasksRVAdapter.SubtasksViewHolder>() {

    inner class SubtasksViewHolder(val bind: SubtasksItemLayoutBinding) :
        RecyclerView.ViewHolder(bind.root) {


        fun bind(position: Int) {
            val subtask = subtasksList[position]




            bind.subtasksEt.setText(subtask.title)

            if (subtask.isCompleted) {
                bind.subtasksTil.startIconDrawable =
                    ContextCompat.getDrawable(context, R.drawable.ic_baseline_checked_circle_24)
                bind.subtasksEt.paintFlags =
                    bind.subtasksEt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                bind.subtasksTil.startIconDrawable =
                    ContextCompat.getDrawable(context, R.drawable.ic_baseline_unchecked_circle_24)
                bind.subtasksEt.paintFlags = 0x00
            }


            bind.subtasksTil.setStartIconOnClickListener {
                subtasksList[position].isCompleted = !subtask.isCompleted
                notifyItemChanged(position)
            }

            bind.subtasksTil.setEndIconOnClickListener {

                subtasksList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, subtasksList.size)
            }
            bind.subtasksEt.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    Toast.makeText(
                        context,
                        "text updated to ${bind.subtasksEt.text.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnEditorActionListener true
                }

                false
            }

        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubtasksRVAdapter.SubtasksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.subtasks_item_layout, parent, false)
        val bind = SubtasksItemLayoutBinding.bind(view)

        return SubtasksViewHolder(bind)
    }

    override fun onBindViewHolder(holder: SubtasksRVAdapter.SubtasksViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return subtasksList.size
    }
}

class SubtasksDiffCallback : DiffUtil.ItemCallback<Subtask>() {
    override fun areItemsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
        return oldItem == newItem
    }
}