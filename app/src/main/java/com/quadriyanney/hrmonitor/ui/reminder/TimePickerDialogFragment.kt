package com.quadriyanney.hrmonitor.ui.reminder

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var mListener: Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            activity, this, hour, minute,
            DateFormat.is24HourFormat(activity)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onTimeSet(timePicker: TimePicker, hour: Int, minute: Int) {
        mListener?.setReminder(hour, minute)
    }

    interface Listener {
        fun setReminder(hour: Int, minute: Int)
    }

    companion object {
        val TAG = TimePickerDialogFragment::class.java.simpleName
    }
}
