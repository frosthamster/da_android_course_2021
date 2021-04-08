package ru.anisimov_d.da_androidcourse_2021.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import ru.anisimov_d.da_androidcourse_2021.R
import ru.anisimov_d.da_androidcourse_2021.domain.HabitType

class HabitsViewFragment : Fragment() {
    class HabitsListPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HabitsListFragment.newInstance(HabitType.GOOD)
                else -> HabitsListFragment.newInstance(HabitType.BAD)
            }
        }

        override fun getItemCount() = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_habits_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pager = view.findViewById<ViewPager2>(R.id.habitsView_pager)
        pager.adapter = HabitsListPagerAdapter(requireActivity())
        TabLayoutMediator(view.findViewById(R.id.habitsView_tabs), pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Хорошие"
                else -> "Плохие"
            }
        }.attach()

        view.findViewById<FloatingActionButton>(R.id.habitsView_fabAdd).setOnClickListener {
            (requireActivity() as ICreateHabitHandler).createHabit()
        }
    }
}