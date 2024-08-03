package com.plearn.jollibeeexam

import android.app.Application
import com.plearn.core.di.coreModule
import com.plearn.data.di.dataModule
import com.plearn.feature.di.featureModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class JollibeeExamApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JollibeeExamApplication)
            modules(listOf(coreModule, dataModule, featureModule))
        }
    }
}