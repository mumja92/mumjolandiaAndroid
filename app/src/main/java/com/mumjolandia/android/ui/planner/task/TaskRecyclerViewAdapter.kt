package com.mumjolandia.android.ui.planner.task

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.R
import com.mumjolandia.android.ui.planner.task.TaskRecyclerViewAdapter.ViewHolder
import com.mumjolandia.android.utils.TaskSupervisorHelper


class TaskRecyclerViewAdapter internal constructor(context: Context?, data: List<String>) : RecyclerView.Adapter<ViewHolder>() {
    private var mData: List<String>
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.recyclerview_planner_row, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.myTextView.text = TaskSupervisorHelper.getTaskNameFromTaskString(mData[position])
        when {
            TaskSupervisorHelper.getTaskDoneFromTaskString(mData[position]) -> {
                holder.myTextView.setBackgroundColor(Color.GREEN)
            }
            TaskSupervisorHelper.getTaskDateStringFromTaskString(mData[position]) == null -> {
                holder.myTextView.setBackgroundColor(Color.WHITE)
            }
            else -> {
                holder.myTextView.setBackgroundColor(Color.YELLOW)
            }
        }
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnLongClickListener {
        var myTextView: TextView = itemView.findViewById(R.id.textViewPlannerRecyclerRow)
        override fun onClick(view: View?) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }

        override fun onLongClick(view: View?): Boolean {
            if (mClickListener != null) mClickListener!!.onItemLongClick(view, adapterPosition)
            return true
        }
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): String {
        return mData[id]
    }

    fun reset(data: List<String>){
        mData = data
        notifyDataSetChanged()
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    // data is passed into the constructor
    init {
        mData = data
    }
}