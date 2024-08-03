package com.plearn.domain.repository

import com.plearn.core.utils.Result
import com.plearn.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
}