package com.plearn.feature.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.plearn.core.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoggedIn) {
        onLoginSuccess(uiState.email)
    } else {
        LoginContent(
            email = uiState.email,
            onEmailChange = viewModel::onEmailChange,
            password = uiState.password,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = viewModel::login,
            isLoading = uiState.isLoading,
            error = uiState.error,
            emailError = uiState.emailError,
            onEmailFocusChanged = { focused ->
                if (!focused) {
                    viewModel.validateEmail()
                }
            }
        )
    }
}

@Composable
fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    error: String?,
    emailError: String?,
    onEmailFocusChanged: (Boolean) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
            },
            label = { Text(stringResource(R.string.hint_email)) },
            isError = emailError != null,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = stringResource(R.string.hint_email_icon)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordFocusRequester.requestFocus()
                }
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester)
                .onFocusChanged { focusState ->
                    onEmailFocusChanged(focusState.isFocused)
                }
        )
        if (emailError != null) {
            Text(text = emailError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.hint_password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = stringResource(R.string.hint_password_icon)
                )
            },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) stringResource(R.string.hint_hide_password)
                else stringResource(R.string.hint_show_password)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            visualTransformation =
            if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onLoginClick()
                }
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(stringResource(R.string.btn_login))
            }
        }
        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}