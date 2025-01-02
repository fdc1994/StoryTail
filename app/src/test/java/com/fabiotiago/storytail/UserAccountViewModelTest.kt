package com.fabiotiago.storytail

import com.fabiotiago.storytail.app.ui.userAccount.UserAccountViewModel
import com.fabiotiago.storytail.app.ui.userAccount.UserAccountViewState
import com.fabiotiago.storytail.domain.repository.Book
import com.fabiotiago.storytail.domain.repository.BooksRepository
import com.fabiotiago.storytail.domain.repository.FavouritesRepository
import com.fabiotiago.storytail.domain.repository.UserRepository
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.fabiotiago.storytail.domain.repository.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class UserAccountViewModelTest {

    private lateinit var userAccountViewModel: UserAccountViewModel
    private val userRepository: UserRepository = mock()
    private val booksRepository: BooksRepository = mock()
    private val favouritesRepository: FavouritesRepository = mock()
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher to the test dispatcher

        // Initialize the ViewModel
        userAccountViewModel = UserAccountViewModel(userRepository, booksRepository, favouritesRepository)
    }

    @Test
    fun `login should emit LoginSuccess when login is successful`() = runTest {
        // Arrange
        val mockBooks = listOf(
            Book(
                id = 1, title = "Book 1", description = "Description", coverName = "Cover",
                readTime = 100, ageGroup = 1, accessLevel = 2, createdAt = "2021-01-01",
                updatedAt = "2021-01-01", author = "Test Author", rating = 4, progress = 60
            ),
            Book(
                id = 2, title = "Book 2", description = "Description", coverName = "Cover",
                readTime = 120, ageGroup = 2, accessLevel = 1, createdAt = "2021-02-01",
                updatedAt = "2021-02-01", author = "Test Author", rating = 5, progress = 100
            )
        )
        whenever(userRepository.authenticateUser(anyOrNull(), anyOrNull(), anyOrNull())).thenAnswer {
            val callback = it.getArgument<((Boolean, String) -> Unit)>(2)
            callback(true, "Login Success")
        }
        whenever(booksRepository.getBooks()).thenReturn(mockBooks)
        whenever(favouritesRepository.getFavourites()).thenReturn(emptyList())
        UserAuthenticationManager.user = User(
            id = 1,
            userName = "testUser",
            email = "email",
            userTypeId = 1,
            firstName = "Test",
            lastName = "User",
            userPhotoUrl = "photo",
            createdAt = "2021-01-01",
            updatedAt = "2021-01-01"
        )

        // Act
        userAccountViewModel.login("testUser", "password")

        // Assert
        val state = userAccountViewModel.viewState.first()
        assert(state is UserAccountViewState.LoginSuccess)
        val content = state as UserAccountViewState.LoginSuccess
        assert(content.username == "testUser")
        assert(content.books?.size == 2) // Books should be loaded
        assert(content.books?.first()?.title == "Book 1")
    }

    @Test
    fun `login should emit LoginError when login fails`() = runTest {
        // Arrange
        whenever(userRepository.authenticateUser(anyOrNull(), anyOrNull(), anyOrNull())).thenAnswer {
            val callback = it.getArgument<((Boolean, String) -> Unit)>(2)
            callback(false, "Invalid Credentials")
        }

        // Act
        userAccountViewModel.login("wrongUser", "wrongPassword")

        // Assert
        val state = userAccountViewModel.viewState.first()
        assert(state is UserAccountViewState.LoginError)
        val errorState = state as UserAccountViewState.LoginError
        assert(errorState.message == "Invalid Credentials")
    }

    @Test
    fun `logout should clear user data and emit Logout state`() = runTest {
        // Arrange
        val mockUser = mock(User::class.java)
        whenever(mockUser.id).thenReturn(0)


        // Act
        userAccountViewModel.logout()

        // Assert
        val state = userAccountViewModel.viewState.first()
        assert(state is UserAccountViewState.Logout)
        assert(!UserAuthenticationManager.isUserLoggedIn)
        assert(UserAuthenticationManager.user == null)
    }

    @Test
    fun `addOrRemoveFavourite should add favourite correctly when isFavourite is false`() = runTest {
        // Arrange
        val bookId = 1
        val userId = 1
        val isFavourite = false
        val mockUser = mock(User::class.java)
        whenever(mockUser.id).thenReturn(userId)
        UserAuthenticationManager.user = mockUser

        // Act
        userAccountViewModel.addOrRemoveFavourite(isFavourite, bookId)

        // Assert
        verify(favouritesRepository).addFavourite(userId, bookId)
    }

    @Test
    fun `addOrRemoveFavourite should remove favourite correctly when isFavourite is true`() = runTest {
        // Arrange
        val bookId = 1
        val userId = 1
        val isFavourite = true
        val mockUser = mock(User::class.java)
        whenever(mockUser.id).thenReturn(userId)
        UserAuthenticationManager.user = mockUser

        // Act
        userAccountViewModel.addOrRemoveFavourite(isFavourite, bookId)

        // Assert
        verify(favouritesRepository).removeFavourite(userId, bookId)
    }

    @Test
    fun `becomePremium should upgrade user when successful`() = runTest {
        // Arrange
        val userId = 0
        whenever(userRepository.upgradeUser(anyOrNull(), anyOrNull())).thenAnswer {
            val callback = it.getArgument<((Boolean, String) -> Unit)>(1)
            callback(true, "Upgrade Successful")
        }

        // Act
        userAccountViewModel.becomePremium(userId)

        // Assert
        val state = userAccountViewModel.viewState.first()
        assert(state is UserAccountViewState.LoginSuccess)
    }
}