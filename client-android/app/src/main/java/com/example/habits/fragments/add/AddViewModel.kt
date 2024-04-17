package com.example.habits.fragments.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.data.models.HabitData
import kotlinx.coroutines.launch
import java.time.DayOfWeek

class AddViewModel : ViewModel() {
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

    private var _repeatDailyIn = MutableLiveData<MutableSet<RepeatDailyIn>>(mutableSetOf())
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