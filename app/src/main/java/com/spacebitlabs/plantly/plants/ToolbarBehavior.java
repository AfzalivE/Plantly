package com.spacebitlabs.plantly.plants;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import timber.log.Timber;

/**
 * Toolbar Behavior to resize CircleImageViews
 * while scrolling
 */

@Keep // might be needed for minification
public class ToolbarBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    private float maxScrollValue;
    private float initialY;
    private int initialImageWidth;
    private int initialImageHeight;

    public ToolbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull LinearLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        if (((ViewGroup) child.getChildAt(2)).getChildAt(0) == null) {
            return false;
        }
        maybeInitProperties(child, dependency);

        float scrollValuePercentage = 1 - (maxScrollValue + dependency.getY()) / maxScrollValue;
        scrollValuePercentage = Math.min(1.5f * scrollValuePercentage, 1); // speed up the animations relative to the movement

//        Timber.d("maxScrollValue: " + scrollValuePercentage);

        child.setY(initialY + dependency.getY());

        ViewGroup todayListView = (ViewGroup) getScrollView(child);
        int childCount = todayListView.getChildCount();

        // resize all the images
        for (int i = 0; i < childCount; i++) {
            ViewGroup todayListViewItem = (ViewGroup) todayListView.getChildAt(i);
            View circleImageView = todayListViewItem.getChildAt(0);
            LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) circleImageView.getLayoutParams();
            lp1.width = (int) (initialImageWidth * Math.min(1, 1 - scrollValuePercentage + 0.5));
            lp1.height = (int) (initialImageHeight * Math.min(1, 1 - scrollValuePercentage + 0.5));

            circleImageView.setLayoutParams(lp1);
        }

        child.setAlpha(1 - scrollValuePercentage);

        return true;
    }

    private void maybeInitProperties(LinearLayout child, View dependency) {
        if (maxScrollValue == 0) {
            maxScrollValue = dependency.getHeight() - dependency.getMinimumHeight();
        }

        if (initialY == 0) {
            initialY = child.getY();
        }

        if (initialImageWidth == 0) {
            initialImageWidth = getTodayItemImage(child).getWidth();
        }

        if (initialImageHeight == 0) {
            initialImageHeight = getTodayItemImage(child).getHeight();
        }
    }

    private View getScrollView(LinearLayout child) {
        return child.getChildAt(2);
    }

    private View getTodayItemImage(LinearLayout child) {
        return ((ViewGroup) ((ViewGroup)
          child.getChildAt(2)) // RecyclerView
                               .getChildAt(0)) // LinearLayout
                                               .getChildAt(0); // CircleImageView;
    }
}
