package com.quadriyanney.hrmonitor.ui.records

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.quadriyanney.hrmonitor.App
import com.quadriyanney.hrmonitor.R
import com.quadriyanney.hrmonitor.helpers.showToast
import com.quadriyanney.hrmonitor.model.Measurement
import kotlinx.android.synthetic.main.activity_records.*
import java.io.File
import javax.inject.Inject

class RecordsActivity : AppCompatActivity(), RecordsContract.RecordsView {

    @Inject
    lateinit var recordsPresenter: RecordsPresenter

    private var records = arrayListOf<Measurement>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        App.getInstance().getAppComponent().inject(this)
        recordsPresenter.attachView(this)

        recordsPresenter.fetchRecords()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_records, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_download -> recordsPresenter.downloadRecords(records)
            R.id.action_share -> recordsPresenter.shareRecords(records)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showRecords(records: List<Measurement>) {
        val adapter = MeasurementsAdapter()
        adapter.setFolders(records)
        this.records = ArrayList(records)

        rvMeasurements.adapter = adapter
        rvMeasurements.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvMeasurements.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun showToastMessage(message: String) {
        showToast(message, Toast.LENGTH_SHORT)
    }

    override fun showSnackbarMessage(message: String, action: String, filePath: String) {
        val snackbar = Snackbar.make(findViewById(R.id.layoutRecords), message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        snackbar.setActionTextColor(resources.getColor(R.color.colorWhite))
        snackbar.setAction(action) {
            val data = FileProvider.getUriForFile(this, "com.quadriyanney.fileprovider", File(filePath))
            grantUriPermission(packageName, data, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val intent = Intent(Intent.ACTION_VIEW)
                .setDataAndType(data, "text/plain")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }
        snackbar.show()
    }

    override fun showProgress(isVisible: Boolean) {
        progressRecords.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        recordsPresenter.detachView()
    }

}
