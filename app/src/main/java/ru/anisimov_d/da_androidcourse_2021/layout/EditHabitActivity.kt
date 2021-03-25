package ru.anisimov_d.da_androidcourse_2021.layout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import ru.anisimov_d.da_androidcourse_2021.R
import ru.anisimov_d.da_androidcourse_2021.domain.Habit
import ru.anisimov_d.da_androidcourse_2021.domain.HabitPriority
import ru.anisimov_d.da_androidcourse_2021.domain.HabitType
import java.util.*


class EditHabitActivity : AppCompatActivity() {
    companion object {
        const val INTENT_EXTRA_TYPE_KEY = "TYPE"
        const val INTENT_TYPE_UPDATE_HABIT = "UPDATE_HABIT"
        const val INTENT_HABIT_DATA = "HABIT_DATA"
    }

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var typeGoodRadioButton: RadioButton
    private lateinit var typeBadRadioButton: RadioButton
    private lateinit var frequencyEditText: EditText
    private lateinit var frequencyRangeDaysEditText: EditText
    private var isAdd: Boolean = true
    private var habitId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_habit)

        nameEditText = findViewById(R.id.editHabit_ptName)
        descriptionEditText = findViewById(R.id.editHabit_ptDescription)
        prioritySpinner = findViewById(R.id.editHabit_spinPriority)
        typeGoodRadioButton = findViewById(R.id.editHabit_rbGood)
        typeBadRadioButton = findViewById(R.id.editHabit_rbBad)
        frequencyEditText = findViewById(R.id.editHabit_ptFrequency)
        frequencyRangeDaysEditText = findViewById(R.id.editHabit_ptFrequencyRangeDays)
        findViewById<Button>(R.id.editHabit_btSave).setOnClickListener {
            val habit = getHabit() ?: return@setOnClickListener
            Intent(this, MainActivity::class.java)
                .apply {
                    putExtra(
                        MainActivity.INTENT_EXTRA_TYPE_KEY,
                        if (isAdd) MainActivity.INTENT_TYPE_ADD_HABIT else MainActivity.INTENT_TYPE_UPDATE_HABIT
                    )
                    putExtra(MainActivity.INTENT_HABIT_DATA, habit)
                }
                .let {
                    setResult(1, it)
                    finish()
                }
        }
        handleIntent()
    }

    private fun handleIntent() {
        when(intent.getStringExtra(INTENT_EXTRA_TYPE_KEY)) {
            INTENT_TYPE_UPDATE_HABIT -> {
                isAdd = false
                setHabit(intent.getParcelableExtra(INTENT_HABIT_DATA)!!)
            }
            else -> return
        }
    }

    private fun setHabit(habit: Habit) {
        habitId = habit.id
        nameEditText.setText(habit.name)
        descriptionEditText.setText(habit.description)
        prioritySpinner.setSelection(habit.priority.toRepresentation())
        when(habit.type) {
            HabitType.GOOD -> typeGoodRadioButton.isActivated = true
            HabitType.BAD -> typeBadRadioButton.isActivated = true
        }
        frequencyEditText.setText(habit.frequency.toString())
        frequencyRangeDaysEditText.setText(habit.frequencyRangeDays.toString())
    }

    private fun getHabit(): Habit? {
        return Habit(
            id = habitId ?: UUID.randomUUID(),
            name = nameEditText.text.toString().takeIf { it.isNotEmpty() } ?: return null,
            description = descriptionEditText.text.toString().takeIf { it.isNotEmpty() } ?: return null,
            priority = HabitPriority.fromRepresentation(prioritySpinner.selectedItem.toString(), resources),
            type = if (typeGoodRadioButton.isActivated) HabitType.GOOD else HabitType.BAD,
            frequency = frequencyEditText.text.toString().takeIf { it.isNotEmpty() } ?.toInt() ?: return null,
            frequencyRangeDays = frequencyRangeDaysEditText.text.toString().takeIf { it.isNotEmpty() } ?.toInt() ?: return null,
        )
    }
}
