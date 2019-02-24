package com.spacebitlabs.plantly.plants

import android.content.Context
import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_plants.view.*
import kotlinx.android.synthetic.main.today_list_item.view.*

@Suppress("unused")
class ToolbarBehavior(context: Context, attrs: AttributeSet) :
    androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<LinearLayout>(context, attrs) {

    private var initialised = false
    private var maxScrollValue = 0f
    private var initialImageWidth = 0f
    private var initialImageHeight = 0f

    override fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: LinearLayout, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: LinearLayout, dependency: View): Boolean {
        maybeInitProperties(child, dependency)

        var scrollValuePercentage = 1 - (maxScrollValue + dependency.y) / maxScrollValue
        scrollValuePercentage = Math.min(1.5f * scrollValuePercentage, 1f) // speed up the animations relative to the movement

//        Timber.d("maxScrollValue: %s", scrollValuePercentage);
//        Timber.d("InitialY: %s", initialY);

        child.y = dependency.y
        child.alpha = 1 - scrollValuePercentage

        if (child.today_list.childCount > 0) {

            val todayListView = child.today_list
            val childCount = todayListView.childCount

            // resize all the images
            for (i in 0 until childCount) {
                val todayListViewItem = todayListView.getChildAt(i) as ViewGroup
                val circleImageView = todayListViewItem.getChildAt(0)
                val lp1 = circleImageView.layoutParams as LinearLayout.LayoutParams
                lp1.width = (initialImageWidth * Math.min(1.0, 1 - scrollValuePercentage + 0.5)).toInt()
                lp1.height = (initialImageHeight * Math.min(1.0, 1 - scrollValuePercentage + 0.5)).toInt()

                circleImageView.layoutParams = lp1
            }
        }

        return true
    }

    private fun maybeInitProperties(child: LinearLayout, dependency: View) {
        if (maxScrollValue == 0f) {
            maxScrollValue = (dependency.height - dependency.minimumHeight).toFloat()
        }

        if (child.today_list.childCount > 0 && initialImageWidth == 0f) {
            initialImageWidth = getTodayItemImage(child).width.toFloat()
        }

        if (child.today_list.childCount > 0 && initialImageHeight == 0f) {
            initialImageHeight = getTodayItemImage(child).height.toFloat()
        }
    }

    private fun getTodayItemImage(child: LinearLayout): View = child.today_list.image
}