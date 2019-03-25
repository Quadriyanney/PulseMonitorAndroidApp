package com.quadriyanney.hrmonitor.di.module

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.quadriyanney.hrmonitor.App
import com.quadriyanney.hrmonitor.util.NetworkUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var app: App) {

    @Provides
    @Singleton
    internal fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideSharedPreference(context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideNetworkUtil(context: Context): NetworkUtil = NetworkUtil(context)

    @Provides
    @Singleton
    fun provideFirebaseDatabaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference

}