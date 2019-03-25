package com.quadriyanney.hrmonitor.di.module

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.database.DatabaseReference
import com.quadriyanney.hrmonitor.ui.main.MainPresenter
import com.quadriyanney.hrmonitor.ui.records.RecordsPresenter
import com.quadriyanney.hrmonitor.ui.reminder.ReminderPresenter
import com.quadriyanney.hrmonitor.util.NetworkUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DependencyModule {

    @Provides
    @Singleton
    fun provideMainPresenter(databaseReference: DatabaseReference, networkUtil: NetworkUtil): MainPresenter =
        MainPresenter(databaseReference, networkUtil)

    @Provides
    @Singleton
    fun provideRecordsPresenter(context: Context, databaseReference: DatabaseReference, networkUtil: NetworkUtil) =
        RecordsPresenter(context, databaseReference, networkUtil)

    @Provides
    @Singleton
    fun provideReminderPresenter(sharedPreferences: SharedPreferences): ReminderPresenter =
        ReminderPresenter(sharedPreferences)
}