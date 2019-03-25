package com.quadriyanney.hrmonitor.ui.main

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.quadriyanney.hrmonitor.model.Measurement
import com.quadriyanney.hrmonitor.helpers.MEASURE
import com.quadriyanney.hrmonitor.helpers.MEASUREMENTS
import com.quadriyanney.hrmonitor.util.NetworkUtil

class MainPresenter(var databaseReference: DatabaseReference,
                    var networkUtil: NetworkUtil) : MainContract.MainPresenter {

    private var mainView: MainContract.MainView? = null
    private var once = true

    override fun attachView(view: MainContract.MainView) {
        mainView = view
    }

    override fun detachView() {
        mainView = null
    }

    override fun takeMeasurement() {
        if (networkUtil.isOnline()) {
            mainView?.showMeasureButton(false)
            mainView?.showMeasurement(false)
            mainView?.showProgress(true)

            databaseReference.child(MEASURE).setValue(true)

            if (once) {
                databaseReference.child(MEASUREMENTS).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val pulses = arrayListOf<Int?>()
                        for (item in dataSnapshot.children) {
                            pulses.add((item.getValue(Measurement::class.java))?.pulse)
                        }

                        val pulse = "${pulses.last()}bpm"

                        mainView?.showProgress(false)
                        mainView?.showMeasureButton(true)
                        mainView?.showMeasurement(true, pulse)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
                once = false
            }
        } else {
            mainView?.showToastMessage("No internet connection")
        }

    }
}