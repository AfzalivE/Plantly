package com.spacebitlabs.plantly.addplant

import android.content.Context
import android.support.transition.ChangeBounds
import android.support.transition.Fade
import android.support.transition.TransitionManager.beginDelayedTransition
import android.support.transition.TransitionSet
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.FrameLayout
import com.spacebitlabs.plantly.R
import kotlinx.android.synthetic.main.view_expandable_input.view.*


/**
 * Created by afzal on 2018-01-01.
 */
class ExpandableInputView : FrameLayout {

    private var isExpanded = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_expandable_input, this)
    }

    fun setValue(name: String): Nothing {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == ACTION_UP) {
            toggle()
        }

        return true
    }

    private fun toggle() = if (isExpanded) collapse() else expand()

    private fun expand() {
        isExpanded = true
        val transition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(Fade(Fade.OUT))
            .addTransition(ChangeBounds())
            .addTransition(Fade(Fade.IN))

        beginDelayedTransition(this, transition)

        collapsed.visibility = View.GONE
        expanded.visibility = View.VISIBLE
    }

    private fun collapse() {
        isExpanded = false
//        TransitionManager.beginDelayedTransition(this)
        collapsed.visibility = View.VISIBLE
        expanded.visibility = View.GONE
    }
}