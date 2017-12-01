package com.spacebitlabs.plantly.calendar

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacebitlabs.plantly.MainViewModel
import com.spacebitlabs.plantly.R
import kotlinx.android.synthetic.main.fragment_calendar.*

/**
 * Created by afzal on 2017-11-29.
 */
class CalendarFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_calendar, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = ViewModelProviders.of(activity).get(MainViewModel::class.java)

        add.setOnClickListener {
            model.setPlants("test")
        }
    }

    companion object {
        fun newInstance(): CalendarFragment {
            val args = Bundle().apply { /* put params */}
            val fragment = CalendarFragment()
            fragment.arguments = args
            return fragment
        }
    }
}