package com.quadriyanney.hrmonitor

import android.app.Application
import com.google.firebase.FirebaseApp
import com.quadriyanney.hrmonitor.di.component.AppComponent
import com.quadriyanney.hrmonitor.di.component.DaggerAppComponent
import com.quadriyanney.hrmonitor.di.module.AppModule
import com.quadriyanney.hrmonitor.di.module.DependencyModule

class App : Application() {

    private lateinit var appComponent: AppComponent

    companion object {
        private lateinit var mInstance: App

        fun getInstance(): App = mInstance
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        FirebaseApp.initializeApp(this)

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dependencyModule(DependencyModule())
            .build()
    }

    fun getAppComponent(): AppComponent = appComponent

}
