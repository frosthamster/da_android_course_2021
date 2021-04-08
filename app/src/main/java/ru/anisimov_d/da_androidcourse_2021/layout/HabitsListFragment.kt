package ru.anisimov_d.da_androidcourse_2021.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.anisimov_d.da_androidcourse_2021.R
import ru.anisimov_d.da_androidcourse_2021.domain.Habit
import ru.anisimov_d.da_androidcourse_2021.domain.HabitType
import java.util.*


class HabitsListFragment : Fragment(), IHabitsListObserver {
    private lateinit var rootHabits: List<Habit>
    private lateinit var habits: MutableList<Habit>
    private lateinit var viewAdapter: HabitRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_habits_list, container, false)
    }

    companion object {
        private const val BUNDLE_TYPE = "type"

        fun newInstance(type: HabitType) =
            HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_TYPE, type)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments!!.getParcelable<HabitType>(BUNDLE_TYPE)!!
        val habitsObservable = requireActivity() as IHabitsListObservable
        habitsObservable.attachHabitObserver(this, type)
        rootHabits = habitsObservable.habits
        habits = rootHabits.filter { it.type == type }.toMutableList()
        viewAdapter = HabitRecyclerViewAdapter(habits, requireActivity() as IEditHabitHandler, requireActivity())
        view.findViewById<RecyclerView>(R.id.habitsList_rwHabits).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = viewAdapter
        }
    }

    override fun onHabitChanged(id: UUID) {
        val index = habits.withIndex().find { indexedValue -> indexedValue.value.id == id }?.index ?: -1
        val target = rootHabits.withIndex().find { it.value.id == id }!!.value
        if (index == -1) {
            habits.add(target)
            viewAdapter.notifyItemInserted(habits.size - 1)
        } else {
            habits[index] = target
            viewAdapter.notifyItemChanged(index)
        }
    }

    override fun onHabitRemoved(id: UUID) {
        val index = habits.withIndex().find { indexedValue -> indexedValue.value.id == id }?.index ?: -1
        if (index != -1) {
            habits.removeAt(index)
            viewAdapter.notifyItemRemoved(index)
        }
    }
}
