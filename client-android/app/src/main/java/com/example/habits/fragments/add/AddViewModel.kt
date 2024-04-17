package com.example.habits.fragments.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.data.models.HabitData
import kotlinx.coroutines.launch
import java.time.DayOfWeek

class AddViewModel: ViewModel(){
    var habit = HabitData("", "", "", "", null, "")

    private var _canSave = MutableLiveData(false)
    val canSave: LiveData<Boolean>
        get() = _canSave

    private var _days = MutableLiveData<MutableSet<DayOfWeek>>(mutableSetOf())
    val days: LiveData<MutableSet<DayOfWeek>>
        get() = _days

    private var _repeatDaily = MutableLiveData<MutableSet<RepeatDaily>>(mutableSetOf())
    val repeatDaily: LiveData<MutableSet<RepeatDaily>>
        get() = _repeatDaily

    fun setRepeatDailys(repeatDailys: MutableSet<RepeatDaily>) {
        viewModelScope.launch {
            _repeatDaily.postValue(repeatDailys)
        }
    }

    fun addRepeatDaily(repeatDaily: RepeatDaily) {
        viewModelScope.launch {
            _repeatDaily.value?.let {
                it.add(repeatDaily)
                _repeatDaily.postValue(it)
            }
        }
    }

    fun removeRepeatDaily(repeatDaily: RepeatDaily) {
        viewModelScope.launch {
            _repeatDaily.value?.let {
                it.remove(repeatDaily)
                _repeatDaily.postValue(it)
            }
        }
    }

    fun setDays(days: MutableSet<DayOfWeek>) {
        viewModelScope.launch {
            _days.postValue(days)
        }
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