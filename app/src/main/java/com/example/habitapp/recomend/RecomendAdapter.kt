package com.example.habitapp.recomend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitapp.R

class RecomendAdapter(
    private val habits: List<Habit>,
    private val onClick: (Habit) -> Unit
) : RecyclerView.Adapter<RecomendAdapter.HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.bind(habit)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.item_tv_title)
        private val ivPriority: ImageView = itemView.findViewById(R.id.item_priority_level)
        private val tvStartTime: TextView = itemView.findViewById(R.id.item_tv_start_time)
        private val tvMinutes: TextView = itemView.findViewById(R.id.item_tv_minutes)

        fun bind(habit: Habit) {
            tvTitle.text = habit.title
            tvStartTime.text = habit.startTime
            tvMinutes.text = habit.minutesFocus.toString()

            when (habit.priorityLevel) {
                HIGH -> ivPriority.setImageResource(R.drawable.ic_priority_high)
                MEDIUM -> ivPriority.setImageResource(R.drawable.ic_priority_medium)
                LOW -> ivPriority.setImageResource(R.drawable.ic_priority_low)
            }

            itemView.setOnClickListener {
                onClick(habit)
            }
        }
    }

    companion object {
        const val LOW = "Low"
        const val MEDIUM = "Medium"
        const val HIGH = "High"
    }
}