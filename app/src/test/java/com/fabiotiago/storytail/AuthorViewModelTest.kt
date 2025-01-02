package com.fabiotiago.storytail

import com.fabiotiago.storytail.app.ui.author.AuthorViewModel
import com.fabiotiago.storytail.domain.repository.Author
import com.fabiotiago.storytail.domain.repository.AuthorRepository
import com.fabiotiago.storytail.domain.repository.Book
import com.fabiotiago.storytail.domain.repository.BooksRepository
import com.fabiotiago.storytail.domain.repository.FavouritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AuthorViewModelTest {

    private lateinit var authorViewModel: AuthorViewModel
    private val authorRepository: AuthorRepository = mock()
    private val favouritesRepository: FavouritesRepository = mock()
    private val bookRepository: BooksRepository = mock()
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher to the test dispatcher

        // Initialize the ViewModel
        authorViewModel = AuthorViewModel(authorRepository, favouritesRepository, bookRepository)
    }

    @Test
    fun `init should load author and books correctly`() = runTest {
        // Arrange
        val mockAuthor = Author(
            id = 1,
            firstName = "Test",
            lastName = "Author",
            description = "Description",
            nationality = "Portuguese"
        )
        val mockBooks = listOf(
            Book(
                id = 1, title = "Book 1",
                description = "Description",
                coverName = "Cover",
                readTime = 100,
                ageGroup = 1,
                accessLevel = 2,
                createdAt = "2021-01-01",
                updatedAt = "2021-01-01",
                author = "Test Author",
                rating = 1,
                progress = 50
            ), Book(
                id = 2,
                title = "Book 2",
                description = "Description",
                coverName = "Cover",
                readTime = 120,
                ageGroup = 2,
                accessLevel = 2,
                createdAt = "2021-01-01",
                updatedAt = "2021-01-01",
                author = "Test Author",
                rating = 5,
                progress = 40
            )
        )
        // Mock repository responses
        whenever(bookRepository.getAuthorBooks(anyInt())).thenReturn(mockBooks)
        `when`(authorRepository.getAuthor(anyInt())).thenReturn(mockAuthor)

        // Call init() method
        authorViewModel.init(1)

        // Collect and assert the emitted state
        val state = authorViewModel.viewState.first()
        assert(state is AuthorViewModel.AuthorViewState.ContentLoaded)
        val content = state as AuthorViewModel.AuthorViewState.ContentLoaded
        assert(content.author?.firstName == "Test")
        assert(content.authorBooks?.size == 2)
        assert(content.authorBooks?.first()?.title == "Book 1")
    }

    @Test
    fun `addOrRemoveFavourite should update favourites correctly`() = runTest {
        val bookId = 1

        // Mock remove and add favourite calls
        whenever(favouritesRepository.getFavourites()).thenReturn(emptyList())
        whenever(authorRepository.getAuthor(anyInt())).thenReturn(
            Author(
                1,
                "Test",
                "Author",
                "Description",
                "Portuguese"
            )
        )
        authorViewModel.init(0)
        // Test add favourite
        authorViewModel.addOrRemoveFavourite(false, bookId)
        verify(favouritesRepository).addFavourite(0, bookId)

        // Test remove favourite
        authorViewModel.addOrRemoveFavourite(true, bookId)
        verify(favouritesRepository).removeFavourite(0, bookId)
    }
}