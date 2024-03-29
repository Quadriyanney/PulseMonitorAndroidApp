package com.quadriyanney.hrmonitor.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Measurement(
    val pulse: Int? = 0,
    val timestamp: Long? = 0L
)