package com.fabiotiago.storytail.app.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fabiotiago.storytail.app.ui.home.Book
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : Fragment() {

    private val viewModel: BookViewModel by viewModels()
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
                ) { Toast.makeText(requireContext(), "Clicked Book", Toast.LENGTH_LONG).show() }
            }

        }
    }
}