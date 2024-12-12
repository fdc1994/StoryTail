package com.fabiotiago.storytail.app.ui.author

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.navArgs
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.app.ui.book.BookComposeUi
import com.fabiotiago.storytail.app.ui.book.BookFragmentArgs
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
                AuthorComposeUi.AuthorDetailsScreen(viewModel, args.authorId)
            }
        }
    }
}