package com.fabiotiago.storytail.ui.userAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserAccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is User Account Fragment"
    }
    val text: LiveData<String> = _text
}