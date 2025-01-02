package com.fabiotiago.storytail

import com.fabiotiago.storytail.app.ui.about.AboutViewModel
import com.fabiotiago.storytail.domain.repository.ContactRepository
import com.fabiotiago.storytail.domain.repository.ContactRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times

@OptIn(ExperimentalCoroutinesApi::class)
class AboutViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var contactRepository: ContactRepository
    private lateinit var viewModel: AboutViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        contactRepository = mock()
        viewModel = AboutViewModel(contactRepository)
    }

    @Test
    fun `onContactFormSubmitted calls sendContactRequest with correct arguments`() = runTest {
        // Arrange
        val name = "John Doe"
        val email = "john.doe@example.com"
        val message = "Hello, this is a test message."

        // Act
        viewModel.onContactFormSubmitted(name, email, message)

        // Advance time to process coroutines
        advanceUntilIdle()

        // Assert
        argumentCaptor<ContactRequest>().apply {
            verify(contactRepository, times(1)).sendContactRequest(capture())
            assert(firstValue.name == name)
            assert(firstValue.email == email)
            assert(firstValue.message == message)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
