package com.example.habits.fragments.add

import androidx.constraintlayout.motion.utils.ViewState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.data.models.Reminder
import com.example.habits.data.models.ReminderEntity
import com.example.habits.utils.SingleLiveEvent
import com.example.habits.utils.ViewString
import com.example.habits.utils.isNow
import com.example.habits.utils.vInteger
import com.example.habits.utils.vString
import kotlinx.coroutines.launch
import java.sql.Time
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

val Reminder.displayTime: ViewString
    get() = when {
        time.isNow() -> vInteger("Now")
        else -> vString(time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
    }

class AddViewModel : ViewModel() {
     var reminder = Reminder()

    private var _reminderLiveData = MutableLiveData<ViewString>()
    val reminderLiveData: LiveData<ViewString>
        get() = _reminderLiveData

    fun updateReminderTime(hourOfDay: Int, minute: Int) {
        val newTime = reminder.time
            .withHour(hourOfDay)
            .withMinute(minute)
        reminder = reminder.copy(time = newTime)
        _reminderLiveData.postValue(reminder.displayTime)
    }

    private val _openTimePickerEvent = SingleLiveEvent<Time>()
    val openTimePickerEvent: LiveData<Time> = _openTimePickerEvent

    fun openTimePicker() {
        with(reminder.time) {
            _openTimePickerEvent.postValue(
                Time(
                    hour = hour,
                    minute = minute
                )
            )
        }
    }

    data class Time(val hour: Int, val minute: Int)
    data class Date(val year: Int, val month: Int, val day: Int)

    data class DateState(
        val time: ViewString,
        val date: ViewString,
    )


    private var _canSave = MutableLiveData(false)
    val canSave: LiveData<Boolean>
        get() = _canSave

    private var _habitGoal = MutableLiveData(HabitGoal.NONE)
    val habitGoal: LiveData<HabitGoal>
        get() = _habitGoal

    private var _days = MutableLiveData(
        mutableSetOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        )
    )
    val days: LiveData<MutableSet<DayOfWeek>>
        get() = _days

    private var _repeatDailyIn = MutableLiveData(
        mutableSetOf(
            RepeatDailyIn.ANYTIME,
        )
    )
    val repeatDailyIn: LiveData<MutableSet<RepeatDailyIn>>
        get() = _repeatDailyIn

    fun setHabitGoal(habitGoal: HabitGoal) {
        viewModelScope.launch {
            _habitGoal.postValue(habitGoal)
        }
    }

    fun setRepeatDailys(repeatDailyIns: MutableSet<RepeatDailyIn>) {
        viewModelScope.launch {
            _repeatDailyIn.postValue(repeatDailyIns)
        }
    }

    fun addRepeatDaily(repeatDailyIn: RepeatDailyIn) {
        viewModelScope.launch {
            _repeatDailyIn.value?.let {
                it.add(repeatDailyIn)
                _repeatDailyIn.postValue(it)
            }
        }
    }

    fun removeRepeatDaily(repeatDailyIn: RepeatDailyIn) {
        viewModelScope.launch {
            _repeatDailyIn.value?.let {
                it.remove(repeatDailyIn)
                _repeatDailyIn.postValue(it)
            }
        }
    }

    fun setDays(days: MutableSet<DayOfWeek>) {
        _days.postValue(days)
    }

    fun addDay(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            _days.value?.let {
                it.add(dayOfWeek)
                _days.postValue(it)
            }
        }
    }

    fun removeDay(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            _days.value?.let {
                it.remove(dayOfWeek)
                _days.postValue(it)
            }
        }
    }

    fun setCanSave(canSave: Boolean) {
        viewModelScope.launch {
            _canSave.postValue(canSave)
        }
    }

}