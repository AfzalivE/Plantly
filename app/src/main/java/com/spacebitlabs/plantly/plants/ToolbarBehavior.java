package com.spacebitlabs.plantly.plants;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import timber.log.Timber;

/**
 * Created by afzal on 2017-12-25.
 */

public class ToolbarBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    private int startYPosition;
    private int startHeight;
    private float maxScrollValue;
    private int initialWidth;
    private int initialHeight;
    private float initialY;
    private float initialX;

    public ToolbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        maybeInitProperties(child, dependency);

        float scrollValuePercentage = 1 - (maxScrollValue + dependency.getY()) / maxScrollValue;
        Timber.d("maxScrollValue: " + scrollValuePercentage);

        child.setY(initialY + startYPosition + dependency.getY());

        ViewGroup todayListView = (ViewGroup) getScrollView(child);
        int childCount = todayListView.getChildCount();

        for (int i = 0; i < childCount; i++) {
            ViewGroup todayListViewItem = (ViewGroup) todayListView.getChildAt(i);
            View circleImageView = todayListViewItem.getChildAt(0);
            LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) circleImageView.getLayoutParams();
            lp1.width = (int) (initialWidth * (1 - scrollValuePercentage));
            lp1.height = (int) (initialHeight * (1 - scrollValuePercentage));

            circleImageView.setLayoutParams(lp1);
        }

        child.setAlpha(1 - scrollValuePercentage);

        return true;
    }

    private void maybeInitProperties(LinearLayout child, View dependency) {
        if (startYPosition == 0)
            startYPosition = (int) (dependency.getY());

        if (startHeight == 0)
            startHeight = child.getHeight();

        if (maxScrollValue == 0) {
//            Toolbar toolbar = (Toolbar) ((ViewGroup) ((ViewGroup) dependency).getChildAt(0)).getChildAt(0);
            Timber.d("Toolbar height");
            maxScrollValue = dependency.getHeight() - dependency.getMinimumHeight();
        }

        if (initialWidth == 0) {
            initialWidth = getTodayScrollViewChild(child).getWidth();
        }

        if (initialHeight == 0) {
            initialHeight = getTodayScrollViewChild(child).getHeight();
        }

        if (initialY == 0) {
            initialY = child.getY();
        }
        if (initialX == 0) {
            initialX = child.getX();
        }
    }

    private View getScrollView(LinearLayout child) {
        return (((ViewGroup) child.getChildAt(2)).getChildAt(0));
    }

    private View getTodayScrollViewChild(LinearLayout child) {
        return ((LinearLayout) ((LinearLayout) ((HorizontalScrollView)
          child.getChildAt(2)) // HorizontalScrollView
                               .getChildAt(0)) // LinearLayout
                                               .getChildAt(0)) // LinearLayout
                                                               .getChildAt(0); // CircleImageView;
    }
}
