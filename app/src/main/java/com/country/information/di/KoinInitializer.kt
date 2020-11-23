package com.country.information.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object KoinInitializer {

    fun initKoin(context: Context) {
        startKoin {
            androidContext(context)
            modules(mutableListOf(
                appModule
            ))
        }
    }

}