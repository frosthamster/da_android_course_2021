package ru.anisimov_d.da_androidcourse_2021.layout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ru.anisimov_d.da_androidcourse_2021.R
import ru.anisimov_d.da_androidcourse_2021.domain.Habit
import ru.anisimov_d.da_androidcourse_2021.domain.HabitPriority
import ru.anisimov_d.da_androidcourse_2021.domain.HabitType
import java.util.*


class EditHabitFragment : Fragment() {
    companion object {
        const val BUNDLE_HABIT = "habit"

        fun newInstance(habit: Habit?) = EditHabitFragment().apply {
            arguments = Bundle().apply { putParcelable(BUNDLE_HABIT, habit) }
        }
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
    private var originalType: HabitType = HabitType.GOOD

    private lateinit var saveHandler: ISaveHabitHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        saveHandler = requireActivity() as ISaveHabitHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = view.findViewById(R.id.editHabit_ptName)
        descriptionEditText = view.findViewById(R.id.editHabit_ptDescription)
        prioritySpinner = view.findViewById(R.id.editHabit_spinPriority)
        typeGoodRadioButton = view.findViewById(R.id.editHabit_rbGood)
        typeBadRadioButton = view.findViewById(R.id.editHabit_rbBad)
        frequencyEditText = view.findViewById(R.id.editHabit_ptFrequency)
        frequencyRangeDaysEditText = view.findViewById(R.id.editHabit_ptFrequencyRangeDays)
        view.findViewById<Button>(R.id.editHabit_btSave).setOnClickListener {
            val habit = getHabit() ?: return@setOnClickListener
            saveHandler.saveHabit(habit, originalType)
        }

        val habit = arguments!!.getParcelable<Habit?>(BUNDLE_HABIT)
        if (habit != null) {
            isAdd = false
            originalType = habit.type
            setHabit(habit)
        }
    }

    private fun setHabit(habit: Habit) {
        habitId = habit.id
        nameEditText.setText(habit.name)
        descriptionEditText.setText(habit.description)
        prioritySpinner.setSelection(habit.priority.toRepresentation())
        when(habit.type) {
            HabitType.GOOD -> typeGoodRadioButton.isChecked = true
            HabitType.BAD -> typeBadRadioButton.isChecked = true
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
            type = if (typeGoodRadioButton.isChecked) HabitType.GOOD else HabitType.BAD,
            frequency = frequencyEditText.text.toString().takeIf { it.isNotEmpty() } ?.toInt() ?: return null,
            frequencyRangeDays = frequencyRangeDaysEditText.text.toString().takeIf { it.isNotEmpty() } ?.toInt() ?: return null,
        )
    }
}
