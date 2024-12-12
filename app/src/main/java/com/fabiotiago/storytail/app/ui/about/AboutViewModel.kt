package com.fabiotiago.storytail.app.ui.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiotiago.storytail.domain.repository.ContactRepository
import com.fabiotiago.storytail.domain.repository.ContactRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val contactRepository: ContactRepository
): ViewModel() {

    fun onContactFormSubmitted(name: String, email:String, message:String) {
        viewModelScope.launch {
            contactRepository.sendContactRequest(ContactRequest(name, email, message))
        }
    }
}