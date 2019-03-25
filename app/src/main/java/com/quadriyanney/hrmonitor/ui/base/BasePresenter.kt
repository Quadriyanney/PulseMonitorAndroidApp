package com.quadriyanney.hrmonitor.ui.base

interface BasePresenter<in I : BaseView> {
    fun attachView(view: I)
    fun detachView()
}