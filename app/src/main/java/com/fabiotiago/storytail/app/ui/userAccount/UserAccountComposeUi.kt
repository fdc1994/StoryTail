package com.fabiotiago.storytail.app.ui.userAccount

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.fabiotiago.storytail.domain.managers.UserAuthenticationManager


object UserAccountComposeUi {

    @Composable
    fun UserAccountScreen(
        viewModel: UserAccountViewModel,
    ) {
        val state by viewModel.viewState.collectAsState(initial = UserAccountViewState.Logout)

        when (state) {
            is UserAccountViewState.Logout -> {
                LoginForm(
                    onLogin = viewModel::login
                )
            }

            is UserAccountViewState.LoginSuccess -> {
                PostLoginScreen(state as UserAccountViewState.LoginSuccess, viewModel::logout, viewModel::becomePremium)
            }

            is UserAccountViewState.Loading -> {
                LoadingView()
            }

            is UserAccountViewState.LoginError -> LoginForm(
                hasError = true,
                onLogin = viewModel::login
            )
        }
    }

    @Composable
    fun LoginForm(
        hasError: Boolean = false,
        errorMessage: String = "Something went wrong, please try again",
        onLogin: (String, String) -> Unit
    ) {
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Error section
            if (hasError) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error",
                                style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                        }
                    }
                }
            }

            // Login Form
            Text(text = "Login", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        onLogin.invoke(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login or Create Account")
            }
            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        Toast.makeText(context, "A reset email has been sent to your email", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Please enter your email to reset password", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recover Password")
            }
        }
    }

    @Composable
    fun PostLoginScreen(
        state: UserAccountViewState.LoginSuccess,
        onLogout: () -> Unit,
        onPremium: (Int) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome, ${state.username}!", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onLogout.invoke() }, modifier = Modifier.fillMaxWidth()) {
                Text("Logout")
            }

            if ((UserAuthenticationManager.user?.userTypeId ?: 1) < 2) {
                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = { UserAuthenticationManager.user?.id?.let { onPremium.invoke(it) } }, modifier = Modifier.fillMaxWidth()) {
                    Text("Become Premium")
                }
            }
        }
    }

    @Composable
    fun LoadingView(
        modifier: Modifier = Modifier,
        loadingText: String? = null,
        textStyle: TextStyle = MaterialTheme.typography.bodySmall,
        textColor: Color = MaterialTheme.colorScheme.onBackground,
        indicatorColor: Color = MaterialTheme.colorScheme.primary,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = indicatorColor)
                loadingText?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = it, style = textStyle, color = textColor)
                }
            }
        }
    }
}