package com.example.habitapp.countdown

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIF_UNIQUE_WORK
import com.example.habitapp.R
import androidx.work.WorkManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.getField
import java.text.SimpleDateFormat
import java.util.*

class CountDownActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var startTimestamp: Date? = null
    private var endTimestamp: Date? = null

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
                val data = Data.Builder()
                    .putString(HABIT_ID, habitId)
                    .putString(HABIT_TITLE, habitTitle)
                    .build()
                val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                    .setInputData(data)
                    .addTag(NOTIF_UNIQUE_WORK)
                    .build()
                workManager.enqueueUniqueWork(NOTIF_UNIQUE_WORK, ExistingWorkPolicy.REPLACE, workRequest)
                updateFirestoreOnStop(habitId)
            }
            updateButtonState(!isCountDownFinished)
        }

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            viewModel.startTimer()

            updateFirestoreOnStart()

            updateButtonState(true)
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            viewModel.resetTimer()

            updateFirestoreOnStop(habitId)

            updateButtonState(false)
            workManager.cancelAllWorkByTag(NOTIF_UNIQUE_WORK)
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }

    private fun updateFirestoreOnStart() {
        startTimestamp = com.google.firebase.Timestamp.now().toDate()
    }

    private fun updateFirestoreOnStop(habitId: String?) {
        endTimestamp = com.google.firebase.Timestamp.now().toDate()
        val user = auth.currentUser

        habitId?.let {
            user?.uid?.let { userId ->
                val habitDataRef = firestore.collection("Users").document(userId)

                habitDataRef.get()
                    .addOnSuccessListener { habitDocSnapshot ->
                        val data = habitDocSnapshot.data
                        val habitStatistic = data?.get(habitId) as? List<Map<String, Any>>

                        val previousTitle: Any? = if (habitStatistic != null) {
                            habitStatistic?.get(0)?.get("title")
                        } else {
                            (data?.get(habitId) as? Map<*, *>)?.get("title")
                        }

                        val previousAdded: Any? = if (habitStatistic != null) {
                            habitStatistic?.get(0)?.get("added")
                        } else {
                            (data?.get(habitId) as? Map<*, *>)?.get("added")
                        }
                        val previousAverageDuration = (data?.get(habitId) as? Map<*, *>)?.get("averageDuration") as? Long
                        val previousTotalUsage = (data?.get(habitId) as? Map<*, *>)?.get("totalUsage") as? Long

                        val durationInMillis =
                            endTimestamp?.time?.minus(startTimestamp?.time ?: 0) ?: 0
                        val newAverageDuration = if (previousAverageDuration == null) {
                            durationInMillis
                        } else {
                            (previousAverageDuration + durationInMillis) / 2
                        }

                        val newTotalUsage = if (previousTotalUsage == null) {
                            1
                        } else {
                            previousTotalUsage + 1
                        }

                        val habitData = hashMapOf(
                            "title" to previousTitle,
                            "added" to previousAdded,
                            "averageDuration" to newAverageDuration,
                            "lastUsage" to endTimestamp,
                            "totalUsage" to newTotalUsage
                        )

                        habitDataRef.update(habitId, habitData)
                    }
            }
        }
    }
}