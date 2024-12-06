package com.fabiotiago.storytail.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabiotiago.storytail.Manager
import com.fabiotiago.storytail.ManagerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val manager: Manager
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = manager.provideText()
    }
    val text: LiveData<String> = _text

    fun getText(): String {
        return "test"
    }
}