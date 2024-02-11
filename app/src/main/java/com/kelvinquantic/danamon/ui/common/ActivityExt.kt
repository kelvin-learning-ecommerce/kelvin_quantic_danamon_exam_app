package com.kelvinquantic.danamon.ui.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.finish(): Unit? = this.findActivity()?.finish()

fun <T> Context.freshStartActivity(activity: Class<T>) {
    val i = Intent(
        this, activity
    )
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

    this.startActivity(i)
}
