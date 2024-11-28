package com.fabiotiago.storytail.ui.userAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class UserAccountFragment : Fragment() {

    private val userAccountViewModel: UserAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val state = userAccountViewModel.userState

        return ComposeView(requireContext()).apply {
            setContent {
                UserAccountComposeUi.UserAccountScreen(
                    state,
                    ::onLogin,
                    ::onLogout
                )
            }
        }
    }

    private fun onLogin(email: String, password: String) =
        userAccountViewModel.onLogin(email, password)

    private fun onLogout() = userAccountViewModel.onLogout()
}