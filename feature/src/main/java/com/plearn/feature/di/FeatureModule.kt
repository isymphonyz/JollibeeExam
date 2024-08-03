package com.plearn.feature.di

import com.plearn.domain.usecase.LoginUseCase
import com.plearn.feature.ui.login.LoginViewModel
import com.plearn.feature.utils.EmailValidator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureModule = module {
    single { EmailValidator() }
    single { LoginUseCase(get()) }
    viewModel { LoginViewModel(get(), get()) }
}