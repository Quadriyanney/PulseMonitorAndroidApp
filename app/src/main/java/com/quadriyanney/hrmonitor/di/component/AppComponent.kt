package com.quadriyanney.hrmonitor.di.component

import com.quadriyanney.hrmonitor.di.module.AppModule
import com.quadriyanney.hrmonitor.di.module.DependencyModule
import com.quadriyanney.hrmonitor.ui.main.MainActivity
import com.quadriyanney.hrmonitor.ui.records.RecordsActivity
import com.quadriyanney.hrmonitor.ui.reminder.ReminderActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (DependencyModule::class)])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(recordsActivity: RecordsActivity)
    fun inject(reminderActivity: ReminderActivity)
}