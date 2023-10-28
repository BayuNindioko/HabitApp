package com.example.habitapp.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.dicoding.habitapp.utils.TimePickerFragment
import com.example.habitapp.MainActivity
import com.example.habitapp.R
import com.example.habitapp.databinding.ActivityDetailBinding
import com.example.habitapp.databinding.ActivityEditHabitBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditHabitActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var binding: ActivityEditHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val habitId = intent.getStringExtra("HABIT_ID")
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
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid
            val firestore = FirebaseFirestore.getInstance()
            if (habitId != null && userId != null) {
                val updatedTitle = binding.addEdTitle.text.toString()
                val updatedStart = binding.addTvStartTime.text.toString()
                val updatedMinutes = binding.addEdMinutesFocus.text.toString().toInt()
                val updatedPriority = binding.spPriorityLevel.selectedItem.toString()

                val habitData = HashMap<String, Any>()
                habitData["title"] = updatedTitle
                habitData["startTime"] = updatedStart
                habitData["minutesFocus"] = updatedMinutes
                habitData["priorityLevel"] = updatedPriority

                firestore.collection(userId)
                    .document(habitId)
                    .update(habitData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Habit updated successfully", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to update habit: $e", Toast.LENGTH_SHORT).show()
                    }
                }
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