package com.fabiotiago.storytail.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fabiotiago.storytail.R
import com.fabiotiago.storytail.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    val homeViewModel:HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null

    val books = listOf(
        Book(0,"Charlotte's Web", "https://example.com/charlottes_web.jpg", false),
        Book(1,"The Gruffalo", "https://example.com/the_gruffalo.jpg", true),
        Book(2,"Flynn's Perfect Pet", "https://example.com/flynns_perfect_pet.jpg", false),
        Book(3, "Freddie and the Fairy", "https://example.com/freddie_and_the_fairy.jpg", false)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreenComposable.HomeScreen(books, ::navigateToBook, ::navigateToLogin)
            }
        }
    }

    private fun navigateToBook(id: Int) {
        findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToBookFragment())
    }

    private fun navigateToLogin() {
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_user_account
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}