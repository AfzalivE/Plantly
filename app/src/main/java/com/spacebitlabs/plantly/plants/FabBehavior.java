package com.spacebitlabs.plantly.plants;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import androidx.annotation.Keep;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * Created by afzal on 2018-01-01.
 */
@Keep
public class FabBehavior extends CoordinatorLayout.Behavior<View> {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private int dySinceDirectionChange;
    private boolean isShowing;
    private boolean isHiding;

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && dySinceDirectionChange < 0 || dy < 0 && dySinceDirectionChange > 0) {
            // We detected a direction change, cancel existing animations and reset our cumulative delta Y
            child.animate().cancel();
            dySinceDirectionChange = 0;
        }

        dySinceDirectionChange += dy;

        if (dySinceDirectionChange > child.getHeight() && child.getVisibility() == View.VISIBLE && !isHiding) {
            hide(child);
        } else if (dySinceDirectionChange < 0 && child.getVisibility() == View.INVISIBLE && !isShowing) {
            show(child);
        }
    }

    /**
     * Hide the quick return view.
     * <p>
     * Animates hiding the view, with the view sliding down and out of the screen.
     * After the view has disappeared, its visibility will change to GONE.
     *
     * @param view The quick return view
     */
    private void hide(final View view) {
        isHiding = true;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        ViewPropertyAnimator animator = view.animate()
                                            .translationY(view.getHeight() + layoutParams.bottomMargin)
                                            .setInterpolator(INTERPOLATOR)
                                            .setDuration(200);

        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Prevent drawing the View after it is gone
                isHiding = false;
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Canceling a hide should show the view
                isHiding = false;
                if (!isShowing) {
                    show(view);
                }
            }
        });

        animator.start();
    }

    /**
     * Show the quick return view.
     * <p>
     * Animates showing the view, with the view sliding up from the bottom of the screen.
     * After the view has reappeared, its visibility will change to VISIBLE.
     *
     * @param view The quick return view
     */
    private void show(final View view) {
        isShowing = true;
        ViewPropertyAnimator animator = view.animate()
                                            .translationY(0)
                                            .setInterpolator(INTERPOLATOR)
                                            .setDuration(200);

        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Canceling a show should hide the view
                isShowing = false;
                if (!isHiding) {
                    hide(view);
                }
            }
        });

        animator.start();
    }
}
