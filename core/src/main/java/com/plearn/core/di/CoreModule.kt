package com.plearn.core.di

import com.plearn.core.utils.ResourceProvider
import org.koin.dsl.module

val coreModule = module {
    single { ResourceProvider(get()) }
}