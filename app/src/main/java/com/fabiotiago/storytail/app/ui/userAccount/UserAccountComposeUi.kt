package com.fabiotiago.storytail.app.ui.userAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fabiotiago.storytail.app.ui.home.HomeViewModel.HomeViewState.Loading


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
                PostLoginScreen(state as UserAccountViewState.LoginSuccess, viewModel::logout)
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
    fun LoginForm(hasError: Boolean = false, errorMessage: String = "Something went wrong, please try again", onLogin: (String, String) -> Unit) {
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

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
                Text("Login")
            }
        }
    }

    @Composable
    fun PostLoginScreen(state: UserAccountViewState.LoginSuccess, onLogout: () -> Unit) {
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