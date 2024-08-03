package com.plearn.feature.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plearn.core.utils.Result
import com.plearn.domain.usecase.LoginUseCase
import com.plearn.feature.utils.EmailValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val emailValidator: EmailValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null, error = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    internal fun validateEmail(): Boolean {
        val email = _uiState.value.email
        return if (email.isNotEmpty() && !emailValidator.isValidEmail(email)) {
            _uiState.value = _uiState.value.copy(emailError = "Invalid email format")
            false
        } else {
            _uiState.value = _uiState.value.copy(emailError = null)
            true
        }
    }

    fun login() {
        if (validateEmail()) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            viewModelScope.launch {
                val result = loginUseCase(_uiState.value.email, _uiState.value.password)
                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true)
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = result.exception.message)
                    }
                }
            }
        }
    }
}