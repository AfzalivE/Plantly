package com.spacebitlabs.plantly.addplant

import android.content.Context
import android.transition.TransitionManager
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
        collapsed.visibility = View.GONE
        expanded.visibility = View.VISIBLE
        TransitionManager.beginDelayedTransition(this)
    }

    private fun collapse() {
        isExpanded = false
        collapsed.visibility = View.VISIBLE
        expanded.visibility = View.GONE
        TransitionManager.beginDelayedTransition(this)
    }
}