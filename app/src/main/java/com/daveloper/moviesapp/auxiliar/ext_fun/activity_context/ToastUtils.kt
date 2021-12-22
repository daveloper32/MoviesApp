package com.daveloper.moviesapp.auxiliar.ext_fun.activity_context

import android.app.Activity
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.widget.Toast

fun Activity.toast (
    message: String,
    long : Int = Toast.LENGTH_SHORT,
    alignCenterText: Boolean = true
) {
    if (alignCenterText) {
        val centerMsgText = SpannableString(message)
        centerMsgText.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            0,
            message.length - 1,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        Toast.makeText(
            this,
            centerMsgText,
            long
        ).show()
    } else {
        Toast.makeText(
            this,
            message,
            long
        ).show()
    }
}