package com.fabiotiago.storytail.app.ui.home

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
import com.fabiotiago.storytail.domain.repository.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreenComposable.HomeScreen(homeViewModel, ::onBookCtaClick, ::navigateToLogin)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.init()
    }

    private fun onBookCtaClick(book: Book) {
        if (book.accessLevel > UserAuthenticationManager.userAccessLevel) {
            navigateToLogin()
        } else {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToBookFragment(book))
        }
    }

    private fun navigateToLogin() {
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_user_account
    }
}