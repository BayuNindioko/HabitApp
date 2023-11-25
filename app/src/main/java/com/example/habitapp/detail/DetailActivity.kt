package com.example.habitapp.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.habitapp.countdown.CountDownActivity
import com.example.habitapp.MainActivity
import com.example.habitapp.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val habitId = intent.getStringExtra("HABIT_ID")
        val habitTitle = intent.getStringExtra("HABIT_TITLE")
        val habitStart = intent.getStringExtra("HABIT_START")
        val habitMinute = intent.getIntExtra("HABIT_MINUTE", 0)
        val habitPriority = intent.getStringExtra("HABIT_PRIORITY")

        binding.title.text = habitTitle
        binding.startText.text = habitStart
        binding.durationText.text = habitMinute.toString()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Detail Habit")
        }

        binding.floatingActionButton3.setOnClickListener {
            val intent = Intent(this, EditHabitActivity::class.java)
            intent.putExtra("HABIT_ID",habitId)
            intent.putExtra("HABIT_TITLE",habitTitle)
            intent.putExtra("HABIT_START", habitStart)
            intent.putExtra("HABIT_MINUTE", habitMinute)
            intent.putExtra("HABIT_PRIORITY",habitPriority)
            startActivity(intent)
        }

        binding.floatingActionButton2.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid
            val firestore = FirebaseFirestore.getInstance()

            if (habitId != null && userId != null) {
                val habitRef = firestore.collection(userId).document(habitId)

                habitRef.get()
                    .addOnSuccessListener { habitDocSnapshot ->
                        val habitData = habitDocSnapshot.data

                        if (habitData != null) {
                            habitRef.delete()
                                .addOnSuccessListener {
                                    val userHabitRef = firestore.collection("Users").document(userId)

                                    userHabitRef.update(habitId, FieldValue.delete())
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Habit deleted successfully", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this, "Failed to delete habit from Users collection: $e", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to delete habit: $e", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to retrieve habit data: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.cdBtn.setOnClickListener {
            val intent = Intent(this, CountDownActivity::class.java)
            intent.putExtra("HABIT_ID",habitId)
            intent.putExtra("HABIT_TITLE",habitTitle)
            intent.putExtra("HABIT_MINUTE", habitMinute)
            startActivity(intent)
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
}