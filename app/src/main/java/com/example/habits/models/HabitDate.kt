package com.example.habits.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HabitDate(
    val day: String,
    val num: String
): Parcelable