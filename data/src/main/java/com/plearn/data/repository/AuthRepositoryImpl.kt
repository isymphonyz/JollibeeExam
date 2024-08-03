package com.plearn.data.repository

import com.plearn.core.utils.ResourceProvider
import com.plearn.domain.model.User
import com.plearn.domain.repository.AuthRepository
import com.plearn.core.utils.Result
import com.plearn.core.R

class AuthRepositoryImpl(private val resourceProvider: ResourceProvider) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> {
        return if (email == "jlbusr@jollibee.com" && password == "p@ssw0rd#1234") {
            Result.Success(User(email, "token"))
        } else {
            Result.Error(Exception(resourceProvider.getString(R.string.error_invalid_credentials)))
        }
    }
}