package com.spacebitlabs.plantly

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.kizitonwose.time.days
import com.kizitonwose.time.milliseconds

/**
 * Extensions
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun dpToPixels(dp: Int): Int {
    val metrics = Resources.getSystem().displayMetrics
    val px = dp * (metrics.densityDpi / 160f)
    return Math.round(px)
}

fun Long.toBundle(name: String): Bundle = Bundle().also { it.putLong(name, this) }

fun Long.millisFreqToDays(): Int {
    return this.milliseconds.inDays.longValue.toInt()
}

private const val DAYS_IN_WEEK = 7

fun String.wordsFreqInMillis(type: String): Long {
    val freqInt = this.toInt()
    return when (type.toLowerCase()) {
        "week", "weeks" -> (freqInt * DAYS_IN_WEEK)
        else            -> freqInt // day case
    }.days.inMilliseconds.longValue
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow((currentFocus ?: return).windowToken, 0)
}
