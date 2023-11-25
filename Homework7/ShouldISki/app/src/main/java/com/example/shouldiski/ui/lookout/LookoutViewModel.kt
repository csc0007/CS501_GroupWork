package com.example.shouldiski.ui.lookout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LookoutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is lookout Fragment"
    }
    val text: LiveData<String> = _text
}