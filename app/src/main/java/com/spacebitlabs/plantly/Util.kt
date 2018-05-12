package com.spacebitlabs.plantly

import android.content.res.Resources
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
