package com.spacebitlabs.plantly.test

import android.os.Bundle
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.transition.TransitionSet
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.spacebitlabs.plantly.R
import kotlinx.android.synthetic.main.activity_test.*

/**
 * Created by afzal on 2018-01-11.
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val transition = TransitionSet()
            .addTransition(ChangeBounds()
                .addTarget(expanded))

        fab.setOnClickListener({
            TransitionManager.beginDelayedTransition(collapsed, transition)
//            collapsed.visibility = View.GONE
            expanded.visibility = View.VISIBLE
        })
    }
}