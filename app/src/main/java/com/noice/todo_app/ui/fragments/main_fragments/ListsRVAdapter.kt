package com.noice.todo_app.ui.fragments.main_fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.noice.todo_app.R
import com.noice.todo_app.databinding.ListItemLayoutBinding

class ListsRVAdapter(val list: MutableList<Pair<Int,String>>): RecyclerView.Adapter<ListsRVAdapter.ListsViewHolder>() {



    inner class ListsViewHolder(val bind: ListItemLayoutBinding):RecyclerView.ViewHolder(bind.root){

        fun bind(position: Int){
            val listName = list[position]

            bind.listItemEt.setText(listName.second)

            bind.listItemTil.setEndIconOnClickListener {

                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, list.size);
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout,parent,false)
        val bind = ListItemLayoutBinding.bind(view)

        return ListsViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ListsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}