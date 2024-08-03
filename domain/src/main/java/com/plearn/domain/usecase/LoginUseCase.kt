package com.plearn.domain.usecase

import com.plearn.core.utils.Result
import com.plearn.domain.model.User
import com.plearn.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }
}