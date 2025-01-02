package com.fabiotiago.storytail.app.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : Fragment() {

    private val args: BookFragmentArgs by navArgs()

    private val viewModel: BookViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        val book = args.book
        if (book.accessLevel > UserAuthenticationManager.userAccessLevel) {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val book = args.book
        return ComposeView(requireContext()).apply {
            setContent {
                BookComposeUi.BookProductPage(
                    viewModel,
                    book,
                    ::openBookReader,
                    ::openAuthorPage
                )
            }
        }
    }

    private fun openBookReader() {
        findNavController().navigate(
            BookFragmentDirections.actionBookFragmentToReadBookFragment(
                args.book.id
            )
        )
    }

    private fun openAuthorPage(authorId: Int) {
        findNavController().navigate(
            BookFragmentDirections.actionBookFragmentToAuthorFragment(
                authorId
            )
        )
    }
}