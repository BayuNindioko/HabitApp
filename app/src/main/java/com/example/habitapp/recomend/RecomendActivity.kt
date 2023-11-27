package com.example.habitapp.recomend

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitapp.MainActivity
import com.example.habitapp.R
import com.example.habitapp.databinding.ActivityRecomendBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class RecomendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecomendBinding
    private var currentSortCriteria = SortCriteria.TITLE

    enum class SortCriteria {
        TITLE,
        PRIORITY,
        MINUTES_FOCUS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecomendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Recommendation Habit")
        }

        setupSortingButton()
        loadRecommendations()
    }

    private fun setupSortingButton() {
        binding.btnSort.setOnClickListener {
            toggleSortCriteria()
            loadRecommendations()
        }
    }

    private fun toggleSortCriteria() {
        currentSortCriteria = when (currentSortCriteria) {
            SortCriteria.TITLE -> SortCriteria.PRIORITY
            SortCriteria.PRIORITY -> SortCriteria.MINUTES_FOCUS
            SortCriteria.MINUTES_FOCUS -> SortCriteria.TITLE
        }
    }

    private fun loadRecommendations() {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val habitsCollection = firestore.collection("Recommend")

            habitsCollection.get()
                .addOnSuccessListener { querySnapshot ->
                    val habitList = mutableListOf<Habit>()

                    for (document in querySnapshot.documents) {
                        val habit = document.toObject(Habit::class.java)
                        if (habit != null) {
                            habitList.add(habit)
                        }
                    }

                    sortHabitList(habitList)

                    val recyclerView = binding.rvRecommendations
                    val habitAdapter = RecomendAdapter(habitList) { habit ->
                        addHabitToFirestore(habit, userId)
                    }
                    recyclerView.adapter = habitAdapter
                    recyclerView.layoutManager = LinearLayoutManager(this)
                }
        }
    }

    private fun addHabitToFirestore(habit: Habit, userId: String) {
        val firestore = FirebaseFirestore.getInstance()
        val userHabitsCollection = firestore.collection(userId)
        val userHistory = firestore.collection("Users").document(userId)

        val currentTime = com.google.firebase.Timestamp.now().toDate()
        val habitData = hashMapOf(
            "title" to habit.title,
            "added" to currentTime,
            "averageDuration" to null,
            "lastUsage" to null,
            "totalUsage" to null
        )

        userHabitsCollection.add(habit)
            .addOnSuccessListener { userHabitDocRef ->
                userHistory.update(userHabitDocRef.id, FieldValue.arrayUnion(habitData))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Habit added successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update habit data", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add habit", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sortHabitList(habitList: MutableList<Habit>) {
        when (currentSortCriteria) {
            SortCriteria.TITLE -> habitList.sortBy { it.title }
            SortCriteria.PRIORITY -> habitList.sortBy { it.priorityLevel }
            SortCriteria.MINUTES_FOCUS -> habitList.sortBy { it.minutesFocus }
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