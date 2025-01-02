package com.fabiotiago.storytail.app.ui.userAccount

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fabiotiago.storytail.app.ui.home.HomeFragmentDirections
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager
import com.fabiotiago.storytail.domain.repository.Book
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAccountFragment : Fragment() {

    private val userAccountViewModel: UserAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return ComposeView(requireContext()).apply {
            setContent {
                UserAccountComposeUi.UserAccountScreen(
                    userAccountViewModel,
                    ::openBrowser,
                    ::onBookCtaClick
                )
            }
        }
    }

    private fun onBookCtaClick(book: Book) {
        if (book.accessLevel > UserAuthenticationManager.userAccessLevel) {
            Toast.makeText(
                requireContext(),
                "Please become premium to access this book",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            findNavController().navigate(
                UserAccountFragmentDirections.actionNavigationUserAccountToBookFragment(
                    book
                )
            )
        }
    }

    private fun openBrowser() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://localhost/storytail/"))
        startActivity(intent)
    }
}