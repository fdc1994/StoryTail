package com.fabiotiago.storytail.ui.userAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.fabiotiago.storytail.databinding.FragmentUserAccountBinding

class UserAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userAccountViewModel: UserAccountViewModel by viewModels()

        return ComposeView(requireContext()).apply {
            setContent {
                UserAccountComposeUi.UserAccountScreen()
            }
        }
    }
}