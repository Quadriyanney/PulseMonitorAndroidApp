package com.quadriyanney.hrmonitor.ui.records

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.quadriyanney.hrmonitor.helpers.MEASUREMENTS
import com.quadriyanney.hrmonitor.model.Measurement
import com.quadriyanney.hrmonitor.util.NetworkUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.util.*

class RecordsPresenter(var context: Context, var databaseReference: DatabaseReference,
                       var networkUtil: NetworkUtil): RecordsContract.RecordsPresenter {

    private var recordsView: RecordsContract.RecordsView? = null

    override fun attachView(view: RecordsContract.RecordsView) {
        recordsView = view
    }

    override fun detachView() {
        recordsView = null
    }

    override fun fetchRecords() {
        if (networkUtil.isOnline()) {
            databaseReference.child(MEASUREMENTS).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val measurements = arrayListOf<Measurement>()
                    for (item in dataSnapshot.children) {
                        val value = item.getValue(Measurement::class.java)
                        measurements.add(value!!)
                    }

                    recordsView?.showProgress(false)
                    recordsView?.showRecords(measurements.reversed())
                }

                override fun onCancelled(error: DatabaseError) {
                    recordsView?.showProgress(false)
                    recordsView?.showToastMessage("Error fetching records")
                }
            })
        } else{
            recordsView?.showProgress(false)
            recordsView?.showToastMessage("No internet connection")
        }
    }

    override fun downloadRecords(records: List<Measurement>) {
        if (records.isNotEmpty()) {
            try {
                recordsView?.showToastMessage("Downloading Records...")

                val filePath = saveToFile(records)

                recordsView?.showSnackbarMessage("Records Downloaded...", "Open", filePath)
            } catch (e: IOException) {
                recordsView?.showToastMessage("Download operation not successful")
            }
        } else {
            recordsView?.showToastMessage("No record to download")
        }
    }

    override fun shareRecords(records: List<Measurement>) {
        val filePath = saveToFile(records)
        val file = File(filePath)

        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(context, "com.quadriyanney.fileprovider", file)
        )

        context.startActivity(Intent.createChooser(sharingIntent, "Share records with"))
    }

    override fun clearRecords() {
        recordsView?.showRecords(ArrayList())
    }

    private fun notesToString(measurements: List<Measurement>): String {
        val text = StringBuilder()
        val calendar = Calendar.getInstance()

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val decimalFormat = DecimalFormat("00")
        val symbols = DateFormatSymbols()
        val months = symbols.months

        for (measurement in measurements) {
            calendar.timeInMillis = measurement.timestamp!!

            val pulse = "${measurement.pulse}bpm"
            val date = "${decimalFormat.format(hour)}:${decimalFormat.format(minute)}," +
                    " ${decimalFormat.format(day)} ${months[month]} $year"

            text.append(pulse).append("\n").append(date).append("\n\n")
        }

        return text.toString()
    }

    private fun saveToFile(records: List<Measurement>): String {
        val text = notesToString(records)

        val folderPath = "${Environment.getExternalStorageDirectory()}/Pulse Monitor"
        val folder = File(folderPath)

        if (!folder.exists()) {
            folder.mkdir()
        }

        val textFileName = "records.txt"
        val textFile = File(folder.absolutePath, textFileName)

        val stream = FileOutputStream(textFile)
        stream.write(text.toByteArray())

        stream.close()

        return textFile.absolutePath
    }
}