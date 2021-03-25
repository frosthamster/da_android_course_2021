package ru.anisimov_d.da_androidcourse_2021.domain

import android.content.res.Resources
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.anisimov_d.da_androidcourse_2021.R
import java.lang.RuntimeException
import java.util.*


enum class HabitPriority {
    LOW,
    MEDIUM,
    HIGH;

    companion object {
        fun fromRepresentation(value: String, resources: Resources): HabitPriority {
            val representation = resources.getStringArray(R.array.editHabit_types)
            return when (value) {
                representation[0] -> LOW
                representation[1] -> MEDIUM
                representation[2] -> HIGH
                else -> throw RuntimeException()
            }
        }
    }

    fun toRepresentation(): Int = when(this) {
            LOW -> 0
            MEDIUM -> 1
            HIGH -> 2
        }
}

enum class HabitType {
    GOOD,
    BAD,
}


@Parcelize
data class Habit(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val frequency: Int,
    val frequencyRangeDays: Int,
): Parcelable
