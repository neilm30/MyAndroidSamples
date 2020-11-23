package com.country.information.app

import android.app.Application
import com.country.information.di.KoinInitializer

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this
        KoinInitializer.initKoin(this)
    }

    companion object {
        lateinit var instance: App
    }

}