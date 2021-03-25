package ru.anisimov_d.da_androidcourse_2021.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


enum class HabitPriority {
    LOW,
    MEDIUM,
    HIGH,
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
