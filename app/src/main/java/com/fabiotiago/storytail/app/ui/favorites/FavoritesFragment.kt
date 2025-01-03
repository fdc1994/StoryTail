package com.fabiotiago.storytail.app.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.fabiotiago.storytail.domain.repository.Book
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                FavoritesComposeUi.FavoritesScreen(favoritesViewModel, ::navigateToBook)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel.init()
    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.init()
    }

    private fun navigateToBook(book: Book) {
        if (book.accessLevel > UserAuthenticationManager.userAccessLevel) {
            navigateToLogin()
        } else {
            findNavController().navigate(
                FavoritesFragmentDirections.actionNavigationFavoritesToBookFragment(
                    book
                )
            )
        }
    }

    private fun navigateToLogin() {
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_user_account
    }
}