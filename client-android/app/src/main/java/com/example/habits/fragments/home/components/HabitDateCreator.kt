package com.example.habits.fragments.home.components

import com.example.habits.data.models.HabitDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class HabitDateCreator {

    companion object {
        fun getListOfHabitDates(): List<HabitDate> {
            val today = LocalDate.now()
            val startDate = today.minusDays(7)

            val dateList = mutableListOf<Pair<LocalDate, String>>()
            for (i in 0 until 13) {
                val currentDate = startDate.plusDays(i.toLong())
                val dayName = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                dateList.add(Pair(currentDate, dayName))
            }

            // Optional: Format the dates if needed
            val formatter = DateTimeFormatter.ofPattern("dd")

            // Print the list of dates and day names
            return dateList.map { (date, dayName) ->
                HabitDate(dayName.take(3), date.format(formatter))
            }
        }
    }

}