package com.fabiotiago.storytail.app.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fabiotiago.storytail.app.ui.home.Book
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : Fragment() {

    companion object {
        fun newInstance() = BookFragment()
    }

    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BookComposeUi.BookProductPage(
                    Book(
                        1,
                        "Title",
                        "Description",
                        null,
                        20,
                        2,
                        2,
                        "",
                        ""
                    )
                ) { Toast.makeText(requireContext(), "Clicked Book", Toast.LENGTH_LONG).show() }
            }

        }
    }
}