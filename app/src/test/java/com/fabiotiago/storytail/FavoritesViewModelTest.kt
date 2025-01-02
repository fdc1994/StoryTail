package com.fabiotiago.storytail

import com.fabiotiago.storytail.app.ui.favorites.FavoritesViewModel
import com.fabiotiago.storytail.domain.repository.Book
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
class FavoritesViewModelTest {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private val favouritesRepository: FavouritesRepository = mock()
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher to the test dispatcher

        // Initialize the ViewModel
        favoritesViewModel = FavoritesViewModel(favouritesRepository)
    }

    @Test
    fun `init should load books correctly when favorites are available`() = runTest {
        // Arrange
        val mockBooks = listOf(
            Book(
                id = 1, title = "Book 1", description = "Description", coverName = "Cover",
                readTime = 100, ageGroup = 1, accessLevel = 2, createdAt = "2021-01-01",
                updatedAt = "2021-01-01", author = "Test Author", rating = 4, progress = 60
            )
        )
        whenever(favouritesRepository.getFavourites()).thenReturn(mockBooks)

        // Act
        favoritesViewModel.init()

        // Assert
        val state = favoritesViewModel.viewState.first()
        assert(state is FavoritesViewModel.FavoritesViewState.ContentLoaded)
        val content = state as FavoritesViewModel.FavoritesViewState.ContentLoaded
        assert(content.books.size == 1)
        assert(content.books.first().title == "Book 1")
    }

    @Test
    fun `init should emit Empty state when favorites list is empty`() = runTest {
        // Arrange
        whenever(favouritesRepository.getFavourites()).thenReturn(emptyList())

        // Act
        favoritesViewModel.init()

        // Assert
        val state = favoritesViewModel.viewState.first()
        assert(state is FavoritesViewModel.FavoritesViewState.Empty)
    }

    @Test
    fun `init should emit Error state when repository returns null`() = runTest {
        // Arrange
        whenever(favouritesRepository.getFavourites()).thenReturn(null)

        // Act
        favoritesViewModel.init()

        // Assert
        val state = favoritesViewModel.viewState.first()
        assert(state is FavoritesViewModel.FavoritesViewState.Error)
    }

    @Test
    fun `addOrRemoveFavourite should remove favorite correctly when isFavourite is true`() = runTest {
        // Arrange
        val bookId = 1
        val userId = 0

        // Act
        favoritesViewModel.addOrRemoveFavourite(true, bookId)

        // Assert
        verify(favouritesRepository).removeFavourite(userId, bookId)
    }

    @Test
    fun `addOrRemoveFavourite should add favorite correctly when isFavourite is false`() = runTest {
        // Arrange
        val bookId = 1
        val userId = 0

        // Act
        favoritesViewModel.addOrRemoveFavourite(false, bookId)

        // Assert
        verify(favouritesRepository).addFavourite(userId, bookId)
    }
}
