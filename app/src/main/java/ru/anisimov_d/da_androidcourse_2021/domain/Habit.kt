package ru.anisimov_d.da_androidcourse_2021.domain

import android.content.res.Resources
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.anisimov_d.da_androidcourse_2021.R
import java.lang.RuntimeException
import java.util.*


@Parcelize
enum class HabitPriority : Parcelable {
    LOW,
    MEDIUM,
    HIGH;

    companion object {
        fun fromRepresentation(value: String, resources: Resources): HabitPriority {
            val representation = resources.getStringArray(R.array.editHabit_priority)
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

@Parcelize
enum class HabitType : Parcelable {
    GOOD,
    BAD;

    companion object {
        fun fromRepresentation(value: String, resources: Resources): HabitType {
            val representation = resources.getStringArray(R.array.editHabit_type)
            return when (value) {
                representation[0] -> HabitType.GOOD
                representation[1] -> HabitType.BAD
                else -> throw RuntimeException()
            }
        }
    }

    fun toRepresentation(): Int = when(this) {
        HabitType.GOOD -> 0
        HabitType.BAD -> 1
    }
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
