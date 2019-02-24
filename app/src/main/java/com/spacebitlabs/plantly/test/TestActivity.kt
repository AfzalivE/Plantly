package com.spacebitlabs.plantly.test

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.dpToPixels
import kotlinx.android.synthetic.main.activity_test.*

/**
 * Created by afzal on 2018-01-11.
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val collapsedSet = ConstraintSet()
        val expandedSet = ConstraintSet()

        collapsedSet.clone(container)

        expandedSet.clone(container)
        expandedSet.setVisibility(R.id.collapsed_value, View.GONE)
        expandedSet.setVisibility(R.id.expanded_value, View.VISIBLE)

        val transition = TransitionSet()
            .addTransition(Fade())
            .addTransition(ChangeBounds())

        fab.setOnClickListener {
            TransitionManager.beginDelayedTransition(container, transition)
            val lp = container.layoutParams
            lp.height = dpToPixels(108)
            container.layoutParams = lp
            expandedSet.applyTo(container)
        }
    }
}