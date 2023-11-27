import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitapp.BottomNav.notifications.DetailStatisticActivity
import com.example.habitapp.BottomNav.notifications.Statistic
import com.example.habitapp.BottomNav.notifications.StatisticAdapter
import com.example.habitapp.databinding.FragmentNotificationsBinding
import com.example.habitapp.detail.DetailActivity
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
            val habitsCollection = firestore.collection("users").document(userId)

            habitsCollection.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val habitList = mutableListOf<Statistic>()

                        val data = documentSnapshot.data?.toMutableMap()
                        data?.remove("nama")

                        val habitIdTitleList = mutableListOf<Pair<String, String>>()

                        for ((habitId, _) in data ?: emptyMap()) {
                            firestore.collection("users").document(userId)
                                .collection(habitId.toString()).document("habitData")
                                .get()
                                .addOnSuccessListener { habitDataSnapshot ->
                                    if (habitDataSnapshot.exists()) {
                                        val title = habitDataSnapshot.getString("title")
                                        habitIdTitleList.add(Pair(habitId.toString(), title ?: ""))

                                        if (habitIdTitleList.size == data?.size ?: 0) {
                                            for ((habitId, title) in habitIdTitleList) {
                                                firestore.collection("Users").document(userId)
                                                    .get()
                                                    .addOnSuccessListener { userDataSnapshot ->
                                                        if (userDataSnapshot.exists()) {
                                                            val habitData = userDataSnapshot.data?.get(habitId) as? Map<*, *>

                                                            val habit = habitData?.let {
                                                                Statistic(
                                                                    habitId,
                                                                    title,
                                                                    it["added"] as Timestamp,
                                                                    (it["averageDuration"] as? Long) ?: 0,
                                                                    it["lastUsage"] as Timestamp,
                                                                    (it["totalUsage"] as? Long) ?: 0
                                                                )
                                                            }

                                                            habit?.habitId = habitId
                                                            habit?.let { habitList.add(it) }

                                                            val recyclerView = binding.rvStatistic
                                                            val statisticAdapter = StatisticAdapter(habitList) { habit ->
                                                                val intent = Intent(context, DetailStatisticActivity::class.java)
                                                                intent.putExtra("HABIT_ID", habit.habitId)
                                                                intent.putExtra("HABIT_TITLE", habit.title)
                                                                intent.putExtra("HABIT_Add", habit.added)
                                                                intent.putExtra("HABIT_Avg", habit.averageDuration)
                                                                intent.putExtra("HABIT_Last", habit.lastUsage)
                                                                intent.putExtra("HABIT_Total", habit.totalUsage)
                                                                startActivity(intent)
                                                            }
                                                            recyclerView.adapter = statisticAdapter
                                                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                                        }
                                                    }
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}