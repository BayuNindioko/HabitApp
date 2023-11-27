package com.example.habitapp.BottomNav.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitapp.databinding.FragmentNotificationsBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val userDocRef = firestore.collection("Users").document(userId)

            userDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val habitList = mutableListOf<Statistic>()

                        val userData = documentSnapshot.data
                        userData?.forEach { (key, value) ->
                            if (key != "nama" && value is Map<*, *>) {
                                val title = value["title"] as? String
                                val added = value["added"] as? Timestamp
                                val averageDuration = value["averageDuration"] as? Long
                                val lastUsage = value["lastUsage"] as? Timestamp
                                val totalUsage = value["totalUsage"] as? Long

                                if (title != null && added != null && averageDuration != null &&
                                    lastUsage != null && totalUsage != null
                                ) {
                                    val statistic = Statistic(
                                        key,
                                        title,
                                        added,
                                        averageDuration,
                                        lastUsage,
                                        totalUsage
                                    )
                                    habitList.add(statistic)
                                }
                            }
                        }

                        val recyclerView = binding.rvStatistic
                        val statisticAdapter = StatisticAdapter(habitList) { habit ->
                            val intent = Intent(context, DetailStatisticActivity::class.java)
                            intent.putExtra("HABIT_TITLE", habit.title)
                            intent.putExtra("HABIT_Add", habit.added?.toDate())
                            intent.putExtra("HABIT_Avg", habit.averageDuration)
                            intent.putExtra("HABIT_Last", habit.lastUsage?.toDate())
                            intent.putExtra("HABIT_Total", habit.totalUsage)
                            startActivity(intent)
                        }
                        recyclerView.adapter = statisticAdapter
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error getting document: $exception")
                }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}