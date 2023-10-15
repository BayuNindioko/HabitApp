package com.example.habitapp.BottomNav.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.BottomNav.addHabit.AddHabitActivity
import com.example.habitapp.Login.SignUpActivity
import com.example.habitapp.databinding.FragmentMyhabitBinding

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