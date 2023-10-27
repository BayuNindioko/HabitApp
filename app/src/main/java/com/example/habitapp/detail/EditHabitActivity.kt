package com.example.habitapp.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.View
import com.dicoding.habitapp.utils.TimePickerFragment
import com.example.habitapp.R
import com.example.habitapp.databinding.ActivityDetailBinding
import com.example.habitapp.databinding.ActivityEditHabitBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditHabitActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var binding: ActivityEditHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val habitTitle = intent.getStringExtra("HABIT_TITLE")
        val habitStart = intent.getStringExtra("HABIT_START")
        val habitMinute = intent.getIntExtra("HABIT_MINUTE", 0)
        val habitPriority = intent.getStringExtra("HABIT_PRIORITY")

        binding.addEdTitle.text = Editable.Factory.getInstance().newEditable(habitTitle)
        binding.addTvStartTime.text = Editable.Factory.getInstance().newEditable(habitStart)
        binding.addEdMinutesFocus.text = Editable.Factory.getInstance().newEditable(habitMinute.toString())

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Edit Habit")
        }

        binding.addButton.setOnClickListener {
            //logic edit disini
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun showTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "timePicker")
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.addTvStartTime.text = dateFormat.format(calendar.time)
    }
}