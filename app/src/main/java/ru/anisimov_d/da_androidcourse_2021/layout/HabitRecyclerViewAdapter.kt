package ru.anisimov_d.da_androidcourse_2021.layout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.anisimov_d.da_androidcourse_2021.R
import ru.anisimov_d.da_androidcourse_2021.domain.Habit

class HabitRecyclerViewAdapter(private val habits: List<Habit>, private val root: IEditHabitHandler, private val rootContext: Context) :
    RecyclerView.Adapter<HabitRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.habit_tvName)
        val descriptionTextView: TextView = view.findViewById(R.id.habit_tvDescription)
        val priorityTextView: TextView = view.findViewById(R.id.habit_tvPriority)
        val typeTextView: TextView = view.findViewById(R.id.habit_tvType)
        val frequencyTextView: TextView = view.findViewById(R.id.habit_tvFrequency)

        var habit: Habit? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val habitView = LayoutInflater.from(parent.context).inflate(R.layout.habit, parent, false)
        val holder = ViewHolder(habitView)
        habitView.setOnClickListener {
            root.editHabit(holder.habit!!)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = habits[position]
        holder.apply {
            this.habit = habit
            nameTextView.text = habit.name
            descriptionTextView.text = habit.description
            priorityTextView.text = habit.priority.toString()
            typeTextView.text = habit.type.toString()
            frequencyTextView.text = (
                    "${habit.frequency} " +
                    "${rootContext.getString(R.string.editHabit_frequencyHint)} " +
                    "${habit.frequencyRangeDays} " +
                    rootContext.getString(R.string.editHabit_frequencyRandeDaysHint)
            )
        }
    }

    override fun getItemCount(): Int = habits.size
}