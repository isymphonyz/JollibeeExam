package com.plearn.data

import com.plearn.core.utils.ResourceProvider
import com.plearn.core.utils.Result
import com.plearn.data.repository.AuthRepositoryImpl
import com.plearn.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import com.plearn.core.R
import com.plearn.domain.model.User

class AuthRepositoryImplTest {

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        resourceProvider = mock(ResourceProvider::class.java)
        authRepository = AuthRepositoryImpl(resourceProvider)
    }

    @Test
    fun `login with correct credentials returns success`() = runBlocking {
        val email = "jlbusr@jollibee.com"
        val password = "p@ssw0rd#1234"
        val expectedUser = User(email, "token")

        val result = authRepository.login(email, password)

        assertTrue(result is Result.Success)
        assertEquals(expectedUser, (result as Result.Success).data)
    }

    @Test
    fun `login with incorrect credentials returns error`() = runBlocking {
        val email = "abc@jollibee.com"
        val password = "somePassword"
        val errorMessage = "Invalid credentials"
        `when`(resourceProvider.getString(R.string.error_invalid_credentials)).thenReturn(errorMessage)

        val result = authRepository.login(email, password)

        assertTrue(result is Result.Error)
        assertEquals(errorMessage, (result as Result.Error).exception.message)
    }
}