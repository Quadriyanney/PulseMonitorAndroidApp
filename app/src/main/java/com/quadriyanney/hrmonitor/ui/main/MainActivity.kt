package com.quadriyanney.hrmonitor.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quadriyanney.hrmonitor.App
import com.quadriyanney.hrmonitor.R
import com.quadriyanney.hrmonitor.helpers.showToast
import com.quadriyanney.hrmonitor.ui.records.RecordsActivity
import com.quadriyanney.hrmonitor.ui.reminder.ReminderActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.MainView {

    @Inject
    lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMeasure.setOnClickListener { mainPresenter.takeMeasurement() }

        App.getInstance().getAppComponent().inject(this)
        mainPresenter.attachView(this)
    }

    override fun showProgress(isVisible: Boolean) {
        progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showMeasureButton(isVisible: Boolean) {
        btnMeasure.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showMeasurement(isVisible: Boolean, value: String) {
        tvPulse.visibility = if (isVisible) {
            tvPulse.text = value
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun showToastMessage(message: String) {
        showToast(message, Toast.LENGTH_SHORT)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_reminder -> startActivity(Intent(this, ReminderActivity::class.java))
            R.id.action_records -> startActivity(Intent(this, RecordsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.detachView()
    }
}
