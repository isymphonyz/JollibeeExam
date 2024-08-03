package com.plearn.domain

import com.plearn.core.utils.Result
import com.plearn.domain.model.User
import com.plearn.domain.repository.AuthRepository
import com.plearn.domain.usecase.LoginUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`

class LoginUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        authRepository = mock(AuthRepository::class.java)
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun `login success`() = runBlocking {
        val email = "test@example.com"
        val password = "password"
        val user = User(email, "fake_token")
        val result = Result.Success(user)

        `when`(authRepository.login(email, password)).thenReturn(result)

        val loginResult = loginUseCase.invoke(email, password)
        assertTrue(loginResult is Result.Success)
        assertEquals(user, (loginResult as Result.Success).data)

        verify(authRepository).login(email, password)
        verifyNoMoreInteractions(authRepository)
    }

    @Test
    fun `login failure`() = runBlocking {
        val email = "test@example.com"
        val password = "wrong_password"
        val exception = Exception("Invalid credentials")
        val result = Result.Error(exception)

        `when`(authRepository.login(email, password)).thenReturn(result)

        val loginResult = loginUseCase.invoke(email, password)

        assertTrue(loginResult is Result.Error)
        assertEquals(exception, (loginResult as Result.Error).exception)

        verify(authRepository).login(email, password)
        verifyNoMoreInteractions(authRepository)
    }
}