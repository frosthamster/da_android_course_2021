package ru.anisimov_d.da_androidcourse_2021.layout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.anisimov_d.da_androidcourse_2021.R
import ru.anisimov_d.da_androidcourse_2021.domain.Habit
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val BUNDLE_HABITS_KEY = "HABITS"
        const val INTENT_EXTRA_TYPE_KEY = "TYPE"
        const val INTENT_TYPE_ADD_HABIT = "ADD_HABIT"
        const val INTENT_TYPE_UPDATE_HABIT = "UPDATE_HABIT"
        const val INTENT_HABIT_DATA = "HABIT_DATA"
    }

    private lateinit var emptyHabitsTextView: TextView
    private lateinit var habitsRecyclerView: RecyclerView

    private val habits: MutableList<Habit> = mutableListOf()
    private val habitsMap: MutableMap<UUID, Pair<Int, Habit>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emptyHabitsTextView = findViewById(R.id.main_twEmptyHabits)
        habitsRecyclerView = findViewById<RecyclerView>(R.id.main_rwHabits).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = HabitRecyclerViewAdapter(habits, this@MainActivity)
        }
        findViewById<FloatingActionButton>(R.id.main_fabAdd).setOnClickListener {
            Intent(this, EditHabitActivity::class.java).let {
                startActivityForResult(it, 1)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArray(BUNDLE_HABITS_KEY, habits.toTypedArray())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        habits.clear()
        habitsMap.clear()
        emptyHabitsTextView.visibility = View.VISIBLE
        savedInstanceState.getParcelableArray(BUNDLE_HABITS_KEY)?.map { addHabit(it as Habit) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                when(data?.getStringExtra(INTENT_EXTRA_TYPE_KEY)) {
                    INTENT_TYPE_ADD_HABIT -> data.getParcelableExtra<Habit>(INTENT_HABIT_DATA)?.let { addHabit(it) }
                    INTENT_TYPE_UPDATE_HABIT -> data.getParcelableExtra<Habit>(INTENT_HABIT_DATA)?.let { updateHabit(it) }
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun addHabit(habit: Habit) {
        habits.add(habit)
        val position = habits.size - 1
        habitsMap[habit.id] = Pair(position, habit)
        emptyHabitsTextView.visibility = View.GONE
        habitsRecyclerView.adapter?.notifyItemInserted(position)
    }

    private fun updateHabit(habit: Habit) {
        habitsMap[habit.id]?.let {
            habits[it.first] = habit
            habitsRecyclerView.adapter?.notifyItemChanged(it.first)
        }
    }
}
