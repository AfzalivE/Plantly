package com.spacebitlabs.plantly.plants

import android.support.design.widget.AppBarLayout
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_appbar.view.*

class OffsetChangeListener : AppBarLayout.OnOffsetChangedListener {

    companion object {
        private const val PERCENTAGE_TO_SHOW_TITLE = 0.6f
        internal const val ALPHA_ANIMATIONS_DURATION = 200
    }

    private var isTitleVisible = false

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(verticalOffset).toFloat() / maxScroll.toFloat()

        handleToolbarTitleVisibility(appBarLayout.title, percentage)
    }

    private fun handleToolbarTitleVisibility(title: TextView, percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE) {
            if (!isTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                isTitleVisible = true
            }
        } else {
            if (isTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                isTitleVisible = false
            }
        }
    }

    private fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE) {
            AlphaAnimation(0f, 1f)
        } else {
            AlphaAnimation(1f, 0f)
        }

        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }
}
