package com.fabiotiago.storytail.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fabiotiago.storytail.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val books = listOf(
            Book("Charlotte's Web", "https://example.com/charlottes_web.jpg", false),
            Book("The Gruffalo", "https://example.com/the_gruffalo.jpg", true),
            Book("Flynn's Perfect Pet", "https://example.com/flynns_perfect_pet.jpg", false),
            Book("Flynn's Perfect Pet", "https://example.com/flynns_perfect_pet.jpg", false),
            Book("Flynn's Perfect Pet", "https://example.com/flynns_perfect_pet.jpg", false),
            Book("Freddie and the Fairy", "https://example.com/freddie_and_the_fairy.jpg", false)
        )
        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreenComposable.HomeScreen(books, ::navigateToBook)
            }
        }
    }

    private fun navigateToBook() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}