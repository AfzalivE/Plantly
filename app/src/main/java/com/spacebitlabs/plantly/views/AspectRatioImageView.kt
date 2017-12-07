package com.spacebitlabs.plantly.views

import android.content.Context
import android.util.AttributeSet

/**
 * An ImageView that keeps asp
 *
 * @author Hannes Dorfmann
 */
class AspectRatioImageView : android.support.v7.widget.AppCompatImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredHeight = (measuredWidth.toDouble() / 0.68636363).toInt()
        setMeasuredDimension(measuredWidth, measuredHeight)
    }
}