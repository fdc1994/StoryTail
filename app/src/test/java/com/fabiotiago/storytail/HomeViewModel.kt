package com.fabiotiago.storytail

import com.fabiotiago.storytail.app.ui.home.AgeGroup
import com.fabiotiago.storytail.app.ui.home.HomeViewModel
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
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel
    private val booksRepository: BooksRepository = mock()
    private val favouritesRepository: FavouritesRepository = mock()
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher to the test dispatcher

        // Initialize the ViewModel
        homeViewModel = HomeViewModel(booksRepository, favouritesRepository)
    }

    @Test
    fun `init should load books correctly when books are available`() = runTest {
        // Arrange
        val mockBooks = listOf(
            Book(
                id = 1, title = "Book 1", description = "Description", coverName = "Cover",
                readTime = 100, ageGroup = 1, accessLevel = 2, createdAt = "2021-01-01",
                updatedAt = "2021-01-01", author = "Test Author", rating = 4, progress = 60
            )
        )
        whenever(booksRepository.getBooks()).thenReturn(mockBooks)
        whenever(booksRepository.getPopularBooks()).thenReturn(mockBooks)
        whenever(favouritesRepository.getFavourites()).thenReturn(mockBooks)

        // Act
        homeViewModel.init()

        // Assert
        val state = homeViewModel.viewState.first()
        assert(state is HomeViewModel.HomeViewState.ContentLoaded)
        val content = state as HomeViewModel.HomeViewState.ContentLoaded
        assert(content.books.size == 1)
        assert(content.books.first().title == "Book 1")
    }

    @Test
    fun `init should emit Error state when books are not available`() = runTest {
        // Arrange
        whenever(booksRepository.getBooks()).thenReturn(null)

        // Act
        homeViewModel.init()

        // Assert
        val state = homeViewModel.viewState.first()
        assert(state is HomeViewModel.HomeViewState.Error)
    }

    @Test
    fun `filter should update the filtered books based on age group and search term`() = runTest {
        // Arrange
        val mockBooks = listOf(
            Book(
                id = 1, title = "Book 1", description = "Description", coverName = "Cover",
                readTime = 100, ageGroup = 2, accessLevel = 2, createdAt = "2021-01-01",
                updatedAt = "2021-01-01", author = "Test Author", rating = 4, progress = 60
            ),
            Book(
                id = 2, title = "Book 2", description = "Description", coverName = "Cover",
                readTime = 100, ageGroup = 1, accessLevel = 2, createdAt = "2021-01-01",
                updatedAt = "2021-01-01", author = "Test Author", rating = 4, progress = 60
            )
        )
        whenever(booksRepository.getBooks()).thenReturn(mockBooks)
        homeViewModel.init()
        homeViewModel.filter(AgeGroup.KIDS, "Book 2")

        // Act
        val state = homeViewModel.viewState.first()

        // Assert
        assert(state is HomeViewModel.HomeViewState.ContentLoaded)
        val content = state as HomeViewModel.HomeViewState.ContentLoaded
        assert(content.books.size == 1)
        assert(content.books.first().title == "Book 2")
    }

    @Test
    fun `addOrRemoveFavourite should remove favourite correctly when isFavourite is true`() = runTest {
        // Arrange
        val bookId = 1
        val userId = 0

        // Act
        homeViewModel.addOrRemoveFavourite(true, bookId)

        // Assert
        verify(favouritesRepository).removeFavourite(userId, bookId)
    }

    @Test
    fun `addOrRemoveFavourite should add favourite correctly when isFavourite is false`() = runTest {
        // Arrange
        val bookId = 1
        val userId = 0

        // Act
        homeViewModel.addOrRemoveFavourite(false, bookId)

        // Assert
        verify(favouritesRepository).addFavourite(userId, bookId)
    }
}