package com.example.habitapp.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.dicoding.habitapp.utils.HABIT_ID
import com.example.habitapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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
            intent.putExtra("HABIT_TITLE",habitTitle)
            intent.putExtra("HABIT_START", habitStart)
            intent.putExtra("HABIT_MINUTE", habitMinute)
            intent.putExtra("HABIT_PRIORITY",habitPriority)
            startActivity(intent)
        }

        binding.floatingActionButton2.setOnClickListener {
            //logix delete disini
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