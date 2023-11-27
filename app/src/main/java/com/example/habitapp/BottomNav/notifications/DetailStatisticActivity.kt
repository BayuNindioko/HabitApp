package com.example.habitapp.BottomNav.notifications

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.example.habitapp.databinding.ActivityDetailStatisticBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DetailStatisticActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStatisticBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStatisticBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Statistic")
        }

        val habitTitle = intent.getStringExtra("HABIT_TITLE") ?: ""
        val addedDate = intent.getSerializableExtra("HABIT_Add") as? Date
        val avgDuration = intent.getLongExtra("HABIT_Avg", 0)
        val lastUsageDate = intent.getSerializableExtra("HABIT_Last") as? Date
        val totalUsage = intent.getLongExtra("HABIT_Total", 0)

        val dateFormat = SimpleDateFormat("MMM d, yyyy, HH:mm:ss", Locale.getDefault())
        val addedString = addedDate?.let { dateFormat.format(it) } ?: "N/A"
        val lastUsageString = lastUsageDate?.let { dateFormat.format(it) } ?: "N/A"
        binding.title.text = habitTitle
        binding.textAdded.text = "Added: $addedString"
        binding.textAvgDuration.text = "Average Duration: $avgDuration"
        binding.textLastUsage.text = "Last Usage: $lastUsageString"
        binding.textTotalUsage.text = "Total Usage: $totalUsage"
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