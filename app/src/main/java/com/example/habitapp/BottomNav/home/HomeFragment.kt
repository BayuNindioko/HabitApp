package com.example.habitapp.BottomNav.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitapp.BottomNav.addHabit.AddHabitActivity
import com.example.habitapp.Login.SignUpActivity
import com.example.habitapp.databinding.FragmentMyhabitBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentMyhabitBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyhabitBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val habitsCollection = firestore.collection(userId)

            habitsCollection.get()
                .addOnSuccessListener { querySnapshot ->
                    val habitList = mutableListOf<Habit>()

                    for (document in querySnapshot.documents) {
                        val habit = document.toObject(Habit::class.java)
                        if (habit != null) {
                            habitList.add(habit)
                        }
                    }

                    val recyclerView = binding.rvHabit
                    val habitAdapter = HabitAdapter(habitList) { habit ->

                    }
                    recyclerView.adapter = habitAdapter
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
                .addOnFailureListener { exception ->

                }
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireActivity(), AddHabitActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}