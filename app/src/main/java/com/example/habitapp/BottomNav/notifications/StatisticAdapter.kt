package com.example.habitapp.BottomNav.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitapp.BottomNav.home.Habit
import com.example.habitapp.R

class StatisticAdapter(
    private val dataList: List<Statistic>,
    private val onClick: (Habit) -> Unit) :
    RecyclerView.Adapter<StatisticAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.item_tv_title_statistic)

        //tambahin logic on click disini
        //pake AI aja gampang, nnti tinggal sesuain ajaa
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_statistic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
//        holder.itemText.text = data
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}