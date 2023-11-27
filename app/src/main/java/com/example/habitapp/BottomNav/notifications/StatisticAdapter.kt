package com.example.habitapp.BottomNav.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitapp.BottomNav.notifications.Statistic
import com.example.habitapp.R

class StatisticAdapter(
    private val dataList: List<Statistic>,
    private val onClick: (Statistic) -> Unit
) : RecyclerView.Adapter<StatisticAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.item_tv_title_statistic)

        private val ivPriority: ImageView = itemView.findViewById(R.id.item_priority_level)

        fun bind(statistic: Statistic) {
            itemText.text = statistic.title

            itemView.setOnClickListener {
                onClick(statistic)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_statistic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
