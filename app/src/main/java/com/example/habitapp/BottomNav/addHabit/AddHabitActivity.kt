package com.example.habitapp.BottomNav.addHabit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.dicoding.habitapp.utils.TimePickerFragment
import com.example.habitapp.R
import com.example.habitapp.databinding.ActivityAddHabitBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddHabitActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var binding: ActivityAddHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Add Habit")
        }

        binding.addButton.setOnClickListener {
            addHabit()
        }

    }

    private fun addHabit() {
        val title = binding.addEdTitle.text.toString()
        val minutesFocus = binding.addEdMinutesFocus.text.toString()
        val startTime = binding.addTvStartTime.text.toString()
        val priorityLevel = binding.spPriorityLevel.selectedItem.toString()
        val id = // tarik id dari firestore
        if (title.isNotEmpty() && minutesFocus.isNotEmpty() && startTime.isNotEmpty() && priorityLevel.isNotEmpty()) {
//            tambahin logic ke firebase berdasarkan id user
//            Toast.makeText(this, "Habit added successfully", Toast.LENGTH_SHORT).show()
//            finish()
        } else {
            Toast.makeText(this, "Habit Cant be Empty", Toast.LENGTH_SHORT).show()
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