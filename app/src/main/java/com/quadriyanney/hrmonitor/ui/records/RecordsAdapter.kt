package com.quadriyanney.hrmonitor.ui.records

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quadriyanney.hrmonitor.R
import com.quadriyanney.hrmonitor.model.Measurement
import kotlinx.android.synthetic.main.item_measurement.view.*
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.util.*

class MeasurementsAdapter : RecyclerView.Adapter<MeasurementsAdapter.ViewHolder>() {

    private var mMeasurements: List<Measurement> = ArrayList()

    fun setFolders(measurements: List<Measurement>) {
        mMeasurements = measurements
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_measurement, parent, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(mMeasurements[position])

    override fun getItemCount(): Int = mMeasurements.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(measurement: Measurement) {
            val pulse = "${measurement.pulse}bpm"
            itemView.tvPulse.text = pulse

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = measurement.timestamp!!

            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val decimalFormat = DecimalFormat("00")
            val symbols = DateFormatSymbols()
            val months = symbols.months

            val date = decimalFormat.format(hour.toLong()) + ":" + decimalFormat.format(minute.toLong()) +
                    ", " + decimalFormat.format(day.toLong()) + " " + months[month] + " " + year
            itemView.tvDate.text = date
        }
    }
}
