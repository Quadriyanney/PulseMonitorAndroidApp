package com.quadriyanney.hrmonitor.ui.reminder

import android.content.SharedPreferences
import com.quadriyanney.hrmonitor.helpers.NotificationHandler
import com.quadriyanney.hrmonitor.helpers.PREFERENCE_REMINDER
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderPresenter(var sharedPreferences: SharedPreferences): ReminderContract.ReminderPresenter {

    private var reminderView: ReminderContract.ReminderView? = null

    override fun attachView(view: ReminderContract.ReminderView) {
        reminderView = view
    }

    override fun detachView() {
        reminderView = null
    }

    override fun initiateReminder(currentReminder: Long, hour: Int, minute: Int) {
        if (currentReminder != -1L) {
            cancelReminder(currentReminder)
        }

        val time = "$hour:$minute"
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = sdf.parse(time)
        val remindAt = date.time

        sharedPreferences.edit().apply {
            putLong(PREFERENCE_REMINDER, remindAt)
        }.apply()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = remindAt

        val reminderText = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

        NotificationHandler.scheduleReminder(
            (remindAt / 1000) - System.currentTimeMillis(), TimeUnit.MILLISECONDS, "$remindAt"
        )

        reminderView?.showReminderDetails(true, reminderText)
        reminderView?.showReminderButton(false)
        reminderView?.updateCurrentReminder(remindAt)
    }

    override fun cancelReminder(currentReminder: Long) {
        NotificationHandler.cancelReminder("$currentReminder")
    }
}