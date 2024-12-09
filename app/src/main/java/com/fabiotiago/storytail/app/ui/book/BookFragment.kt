package com.fabiotiago.storytail.app.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : Fragment() {

    private val args: BookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val book = args.book
        return ComposeView(requireContext()).apply {
            setContent {
                BookComposeUi.BookProductPage(
                    book
                ) { openBookReader() }
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
}