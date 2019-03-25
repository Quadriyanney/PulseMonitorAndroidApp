package com.quadriyanney.hrmonitor.ui.main

import com.quadriyanney.hrmonitor.ui.base.BasePresenter
import com.quadriyanney.hrmonitor.ui.base.BaseView

interface MainContract {

    interface MainPresenter: BasePresenter<MainView> {
        fun takeMeasurement()
    }

    interface MainView: BaseView {
        fun showProgress(isVisible: Boolean)
        fun showMeasureButton(isVisible: Boolean)
        fun showMeasurement(isVisible: Boolean, value: String = "")
        fun showToastMessage(message: String)
    }
}