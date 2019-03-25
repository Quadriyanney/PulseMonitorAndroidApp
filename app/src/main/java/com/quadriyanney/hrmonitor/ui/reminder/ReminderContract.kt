package com.quadriyanney.hrmonitor.ui.reminder

import com.quadriyanney.hrmonitor.ui.base.BasePresenter
import com.quadriyanney.hrmonitor.ui.base.BaseView

interface ReminderContract {

    interface ReminderPresenter: BasePresenter<ReminderView> {
        fun initiateReminder(currentReminder: Long, hour: Int, minute: Int)
        fun cancelReminder(currentReminder: Long)
    }

    interface ReminderView: BaseView {
        fun showReminderButton(isVisible: Boolean)
        fun showReminderDetails(isVisible: Boolean, value: String = "")
        fun updateCurrentReminder(currentReminder: Long)
    }
}