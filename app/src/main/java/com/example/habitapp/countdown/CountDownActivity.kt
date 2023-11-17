package com.example.habitapp.countdown

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIF_UNIQUE_WORK
import com.example.habitapp.R
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import java.text.SimpleDateFormat
import java.util.*

class CountDownActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Store the reference to the habit data in Firestore
    private var habitDataRef: DocumentReference? = null

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

        viewModel.eventCountDownFinish.observe(this) { isCountDownFinished ->
            if (isCountDownFinished) {
                updateFirestoreOnStop()
            }
            updateButtonState(!isCountDownFinished)
        }

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            viewModel.startTimer()

            updateFirestoreOnStart(habitId)

            updateButtonState(true)
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            viewModel.resetTimer()

            updateFirestoreOnStop()

            updateButtonState(false)
            workManager.cancelAllWorkByTag(NOTIF_UNIQUE_WORK)
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }

    private fun updateFirestoreOnStart(habitId: String?) {
        val user = auth.currentUser
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        habitId?.let {
            user?.uid?.let { userId ->
                val historyRef = firestore.collection("History").document(userId)

                val habitData = hashMapOf(
                    "startTimestamp" to FieldValue.serverTimestamp(),
                    "endTimestamp" to null,
                    "habitID" to habitId
                )

                historyRef.collection(currentDate).add(habitData)
                    .addOnSuccessListener { documentReference ->
                        habitDataRef = documentReference
                    }
            }
        }
    }

    private fun updateFirestoreOnStop() {
        habitDataRef?.update("endTimestamp", FieldValue.serverTimestamp())
    }
}