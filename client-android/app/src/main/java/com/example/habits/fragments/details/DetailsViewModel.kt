package com.example.habits.fragments.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailsViewModel: ViewModel() {

    private val _trackVisible = MutableLiveData(false)
    val trackVisible: LiveData<Boolean>
        get() = _trackVisible

    fun setTrackVisible(trackVisible: Boolean) {
        viewModelScope.launch {
            _trackVisible.postValue(trackVisible)
        }
    }

}