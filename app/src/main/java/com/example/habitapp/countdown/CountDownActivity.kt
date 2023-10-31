package com.example.habitapp.countdown
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.dicoding.habitapp.utils.NOTIF_UNIQUE_WORK
import com.example.habitapp.R

class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habitId = intent.getStringExtra("HABIT_ID")
        val habitTitle = intent.getStringExtra("HABIT_TITLE")
        val habitMinute = intent.getIntExtra("HABIT_MINUTE", 0).toLong()

        findViewById<TextView>(R.id.tv_count_down_title).text = habitTitle

        val viewModel = ViewModelProvider(this)[CountDownViewModel::class.java]

        val workManager = WorkManager.getInstance(applicationContext)


        viewModel.setInitialTime(habitMinute)
        viewModel.currentTimeString.observe(this) { currentTimeString ->
            findViewById<TextView>(R.id.tv_count_down).text = currentTimeString
        }

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            viewModel.startTimer()
            updateButtonState(true)
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {

            viewModel.resetTimer()
            updateButtonState(false)
            workManager.cancelAllWorkByTag(NOTIF_UNIQUE_WORK)
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }


}