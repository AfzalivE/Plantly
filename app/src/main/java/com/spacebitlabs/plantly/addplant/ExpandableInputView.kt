package com.spacebitlabs.plantly.addplant

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.content.Context
import android.support.transition.*
import android.support.transition.TransitionManager.beginDelayedTransition
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.dpToPixels
import kotlinx.android.synthetic.main.view_expandable_input.view.*


/**
 * Created by afzal on 2018-01-01.
 */
class ExpandableInputView : FrameLayout, View.OnClickListener {
    private var isExpanded = false

    private var isAnimating = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_expandable_input, this)

        layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        setOnClickListener(this)
    }

    fun setValue(name: String): Nothing {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View?) {
        if (!isAnimating) {
            toggle()
        }
    }

    private fun toggle() = if (isExpanded) collapse() else expand()

    private fun expand() {
        isAnimating = true
        isExpanded = true
        val transition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(Fade())
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
            .addListener(endTransition)

        val zAnimator = ObjectAnimator.ofFloat(this, "translationZ", 8f)
        zAnimator.start()

        val lp = container.layoutParams
        lp.height = dpToPixels(108)
        container.layoutParams = lp

        val marginLp = layoutParams as MarginLayoutParams
        marginLp.topMargin = dpToPixels(16)
        marginLp.bottomMargin = dpToPixels(16)
        marginLp.marginStart = dpToPixels(16)
        marginLp.marginEnd = dpToPixels(16)
        layoutParams = marginLp

        beginDelayedTransition(this, transition)
    }

    private fun collapse() {
        isAnimating = true
        isExpanded = false
        val transition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(Fade())
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
            .addListener(endTransition)

        val zAnimator = ObjectAnimator.ofFloat(this, "translationZ", 0f)
        zAnimator.start()

        val lp = container.layoutParams
        lp.height = dpToPixels(56)
        container.layoutParams = lp

        val marginLp = layoutParams as MarginLayoutParams
        marginLp.topMargin = dpToPixels(0)
        marginLp.bottomMargin = dpToPixels(0)
        marginLp.marginStart = dpToPixels(0)
        marginLp.marginEnd = dpToPixels(0)
        layoutParams = marginLp

        TransitionManager.beginDelayedTransition(this, transition)
    }

    private val endTransition = object : TransitionListenerAdapter() {
        override fun onTransitionEnd(transition: Transition) {
            isAnimating = false
        }
    }
}