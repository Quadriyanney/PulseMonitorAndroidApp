package com.quadriyanney.hrmonitor.ui.reminder

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.quadriyanney.hrmonitor.App
import com.quadriyanney.hrmonitor.R
import com.quadriyanney.hrmonitor.helpers.PREFERENCE_REMINDER
import kotlinx.android.synthetic.main.activity_reminder.*
import java.util.*
import javax.inject.Inject

class ReminderActivity : AppCompatActivity(), TimePickerDialogFragment.Listener, ReminderContract.ReminderView {

    private var mCurrentReminder: Long? = null

    @Inject
    lateinit var reminderPresenter: ReminderPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        App.getInstance().getAppComponent().inject(this)
        reminderPresenter.attachView(this)

        mCurrentReminder = reminderPresenter.sharedPreferences.getLong(PREFERENCE_REMINDER, -1L)

        if (mCurrentReminder != -1L) {
            showReminderButton(false)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = mCurrentReminder!!

            val reminderText = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
            tvReminderValue.text = reminderText
        } else {
            showReminderDetails(false)
        }

        btnSetReminder.setOnClickListener {
            TimePickerDialogFragment().show(supportFragmentManager, TimePickerDialogFragment.TAG)
        }

        btnEditReminder.setOnClickListener {
            TimePickerDialogFragment().show(supportFragmentManager, TimePickerDialogFragment.TAG)
        }
    }

    override fun setReminder(hour: Int, minute: Int) {
        reminderPresenter.initiateReminder(mCurrentReminder!!, hour, minute)
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (mCurrentReminder != -1L) {
            menuInflater.inflate(R.menu.activity_reminder, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun showReminderButton(isVisible: Boolean) {
        btnSetReminder.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showReminderDetails(isVisible: Boolean, value: String) {
        tvReminderValue.text = value
        tvRemindMeEvery.visibility = if (isVisible) View.VISIBLE else View.GONE
        tvReminderValue.visibility = if (isVisible) View.VISIBLE else View.GONE
        btnEditReminder.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun updateCurrentReminder(currentReminder: Long) {
        mCurrentReminder = currentReminder
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_cancel_reminder -> {
                reminderPresenter.cancelReminder(mCurrentReminder!!)

                mCurrentReminder = -1L
                invalidateOptionsMenu()

                showReminderDetails(false)
                showReminderButton(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        reminderPresenter.detachView()
    }
}
