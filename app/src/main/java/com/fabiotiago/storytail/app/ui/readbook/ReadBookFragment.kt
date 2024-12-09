package com.fabiotiago.storytail.app.ui.readbook

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.navArgs
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.app.ui.book.BookFragmentArgs
import com.fabiotiago.storytail.app.ui.readbook.ReadBookComposeUi.PdfViewerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadBookFragment : Fragment() {

    private val viewModel: ReadBookViewModel by viewModels()

    private val args: ReadBookFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(args.bookId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PdfViewerScreen(viewModel)
            }
        }
    }
}