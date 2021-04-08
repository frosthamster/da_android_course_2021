package ru.anisimov_d.da_androidcourse_2021.layout

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import ru.anisimov_d.da_androidcourse_2021.R
import ru.anisimov_d.da_androidcourse_2021.domain.Habit
import ru.anisimov_d.da_androidcourse_2021.domain.HabitType
import java.util.*


interface ICreateHabitHandler {
    fun createHabit()
}

interface IEditHabitHandler {
    fun editHabit(habit: Habit)
}

interface ISaveHabitHandler {
    fun saveHabit(habit: Habit, originalType: HabitType)
}

interface IHabitsListObserver {
    fun onHabitChanged(id: UUID)
    fun onHabitRemoved(id: UUID)
}

interface IHabitsListObservable {
    val habits: List<Habit>
    fun attachHabitObserver(observer: IHabitsListObserver, type: HabitType)
}

class MainActivity :
    AppCompatActivity(),
    ICreateHabitHandler,
    IEditHabitHandler,
    ISaveHabitHandler,
    IHabitsListObservable,
    NavigationView.OnNavigationItemSelectedListener
{
    companion object {
        const val BUNDLE_HABITS_KEY = "habits"
    }

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private val typedObservers: MutableMap<HabitType, IHabitsListObserver> = mutableMapOf()
    override val habits: MutableList<Habit> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.main_drawerLayout)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        findViewById<NavigationView>(R.id.main_drawer).setNavigationItemSelectedListener(this)

        setHabitsFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArray(BUNDLE_HABITS_KEY, habits.toTypedArray())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        habits.clear()
        savedInstanceState.getParcelableArray(BUNDLE_HABITS_KEY)?.map { habits.add(it as Habit) }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_home -> setHabitsFragment()
            R.id.menu_item_about -> setInfoFragment()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setDrawerEnabled(enabled: Boolean) {
        val lockMode = if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawerLayout.setDrawerLockMode(lockMode)
        drawerToggle.isDrawerIndicatorEnabled = enabled
    }

    private fun setHabitsFragment() {
        setDrawerEnabled(true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, HabitsViewFragment())
            .commit()
    }

    private fun setInfoFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, InfoFragment())
            .commit()
    }

    override fun attachHabitObserver(observer: IHabitsListObserver, type: HabitType) {
        typedObservers[type] = observer
    }

    override fun editHabit(habit: Habit) {
        setDrawerEnabled(false)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, EditHabitFragment.newInstance(habit))
            .addToBackStack(null)
            .commit()
    }

    override fun createHabit() {
        setDrawerEnabled(false)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, EditHabitFragment.newInstance(null))
            .addToBackStack(null)
            .commit()
    }

    override fun saveHabit(habit: Habit, originalType: HabitType) {
        setDrawerEnabled(true)
        val existingHabit = habits.withIndex().find { it.value.id == habit.id }
        if (existingHabit != null) {
            habits[existingHabit.index] = habit
        } else {
            habits.add(habit)
        }
        if (originalType != habit.type)
            typedObservers[originalType]!!.onHabitRemoved(habit.id)
        typedObservers[habit.type]?.onHabitChanged(habit.id)
        supportFragmentManager.popBackStack()
    }
}
