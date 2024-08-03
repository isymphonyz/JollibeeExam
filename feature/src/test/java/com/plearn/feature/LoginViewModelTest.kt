package com.plearn.feature

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.plearn.core.utils.Result
import com.plearn.domain.model.User
import com.plearn.domain.usecase.LoginUseCase
import com.plearn.feature.ui.login.LoginViewModel
import com.plearn.feature.utils.EmailValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var viewModel: LoginViewModel
    private lateinit var emailValidator: EmailValidator

    @Before
    fun setUp() {
        loginUseCase = mock()
        emailValidator = mock()
        viewModel = LoginViewModel(loginUseCase, emailValidator)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        testDispatcher.scheduler.advanceUntilIdle()
        Dispatchers.resetMain()
    }

    @Test
    fun `onEmailChange updates email and clears errors`() {
        val email = "jlb@jollibee.com"
        viewModel.onEmailChange(email)
        val state = viewModel.uiState.value
        assertEquals(email, state.email)
        assertNull(state.emailError)
        assertNull(state.error)
    }

    @Test
    fun `onPasswordChange updates password and clears error`() {
        val password = "p@ssw0rd#1234"
        viewModel.onPasswordChange(password)
        val state = viewModel.uiState.value
        assertEquals(password, state.password)
        assertNull(state.error)
    }

    @Test
    fun `validateEmail returns false for invalid email`() {
        val invalidEmail = "someEmail"
        viewModel.onEmailChange(invalidEmail)
        `when`(emailValidator.isValidEmail(invalidEmail)).thenReturn(false)

        val isValid = viewModel.validateEmail()
        val state = viewModel.uiState.value
        assertEquals(false, isValid)
        assertEquals("Invalid email format", state.emailError)
    }

    @Test
    fun `validateEmail returns true for valid email`() {
        val validEmail = "jlb@jollibee.com"
        viewModel.onEmailChange(validEmail)
        `when`(emailValidator.isValidEmail(validEmail)).thenReturn(true)

        val isValid = viewModel.validateEmail()
        val state = viewModel.uiState.value
        assertEquals(true, isValid)
        assertNull(state.emailError)
    }

    @Test
    fun `login sets loading and invokes use case on success`() = runTest {
        val email = "jlb@jollibee.com"
        val password = "p@ssw0rd#1234"
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        `when`(emailValidator.isValidEmail(email)).thenReturn(true)
        `when`(loginUseCase.invoke(email, password)).thenReturn(Result.Success(User(email, "token")))

        viewModel.login()

        verify(loginUseCase).invoke(email, password)
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertTrue(state.isLoggedIn)
        assertNull(state.error)
    }

    @Test
    fun `login sets error on use case failure`() = runTest {
        val email = "abc@jollibee.com"
        val password = "somePassword"
        val exception = Exception("Invalid credentials")
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        `when`(emailValidator.isValidEmail(email)).thenReturn(true)
        `when`(loginUseCase.invoke(email, password)).thenReturn(Result.Error(exception))

        viewModel.login()

        verify(loginUseCase).invoke(email, password)
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals("Invalid credentials", state.error)
        assertEquals(false, state.isLoggedIn)
    }

    @Test
    fun `login does not proceed if email is invalid`() = runTest {
        val invalidEmail = "someEmail"
        viewModel.onEmailChange(invalidEmail)
        `when`(emailValidator.isValidEmail(invalidEmail)).thenReturn(false)

        viewModel.login()

        verify(loginUseCase, never()).invoke(anyString(), anyString())
        val state = viewModel.uiState.value
        assertEquals("Invalid email format", state.emailError)
        assertEquals(false, state.isLoggedIn)
    }
}