package com.fabiotiago.storytail.app.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fabiotiago.storytail.app.ui.home.HomeScreenComposable
import com.fabiotiago.storytail.domain.repository.Book
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
                HomeScreenComposable.FavoritesScreen(favoritesViewModel, ::navigateToBook)
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
        findNavController().navigate(
            FavoritesFragmentDirections.actionNavigationFavoritesToBookFragment(
                book
            )
        )
    }
}