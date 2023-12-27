package com.example.habits.models

import android.os.Parcelable
import com.example.habits.fragments.home.components.HabitIntervals
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val id: Int,
    val title: String,
    val interval: HabitIntervals,
    val date: HabitDate
): Parcelable