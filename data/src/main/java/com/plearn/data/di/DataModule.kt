package com.plearn.data.di

import com.plearn.data.repository.AuthRepositoryImpl
import com.plearn.domain.repository.AuthRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}