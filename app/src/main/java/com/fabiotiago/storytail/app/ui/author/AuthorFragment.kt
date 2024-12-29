package com.fabiotiago.storytail.app.ui.author

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.fabiotiago.storytail.domain.repository.Book
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorFragment : Fragment() {

    private val viewModel: AuthorViewModel by viewModels()

    private val args: AuthorFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AuthorComposeUi.AuthorDetailsScreen(viewModel, args.authorId, ::onBookCtaClick)
            }
        }
    }

    private fun onBookCtaClick(book: Book) {
        if (book.accessLevel > UserAuthenticationManager.userAccessLevel) {
            navigateToLogin()
        } else {
            findNavController().navigate(
                AuthorFragmentDirections.actionAuthorFragmentToReadBookFragment(
                    book.id
                )
            )
        }
    }

    private fun navigateToLogin() {
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_user_account
    }
}