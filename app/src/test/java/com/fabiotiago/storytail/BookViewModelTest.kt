package com.fabiotiago.storytail

import com.fabiotiago.storytail.app.ui.book.BookViewModel
import com.fabiotiago.storytail.domain.repository.Book
import com.fabiotiago.storytail.domain.repository.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class BookViewModelTest {

    private lateinit var bookViewModel: BookViewModel
    private val userRepositoryImpl: UserRepositoryImpl = mock()
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher to the test dispatcher

        // Initialize the ViewModel
        bookViewModel = BookViewModel(userRepositoryImpl)
    }

    @Test
    fun `rateBook should call updateUserRating correctly`() = runTest {
        // Arrange
        val book = Book(
            id = 1,
            title = "Test Book",
            description = "Description",
            coverName = "Cover",
            readTime = 100,
            ageGroup = 1,
            accessLevel = 2,
            createdAt = "2021-01-01",
            updatedAt = "2021-01-01",
            author = "Test Author",
            rating = 5,
            progress = 60
        )
        val rating = 4

        // Act
        bookViewModel.rateBook(rating, book)

        // Assert
        verify(userRepositoryImpl).updateUserRating(rating, book.id)
    }
}