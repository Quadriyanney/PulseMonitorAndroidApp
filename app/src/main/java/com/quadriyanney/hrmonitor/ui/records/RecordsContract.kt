package com.quadriyanney.hrmonitor.ui.records

import com.quadriyanney.hrmonitor.model.Measurement
import com.quadriyanney.hrmonitor.ui.base.BasePresenter
import com.quadriyanney.hrmonitor.ui.base.BaseView

interface RecordsContract {

    interface RecordsPresenter: BasePresenter<RecordsView> {
        fun fetchRecords()
        fun downloadRecords(records: List<Measurement>)
        fun shareRecords(records: List<Measurement>)
        fun clearRecords()
    }

    interface RecordsView: BaseView {
        fun showRecords(records: List<Measurement>)
        fun showToastMessage(message: String)
        fun showSnackbarMessage(message: String, action: String, filePath: String)
        fun showProgress(isVisible: Boolean)
    }
}