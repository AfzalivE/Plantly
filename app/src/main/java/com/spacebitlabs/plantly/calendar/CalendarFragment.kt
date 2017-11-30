package com.spacebitlabs.plantly.calendar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacebitlabs.plantly.R

/**
 * Created by afzal on 2017-11-29.
 */
class CalendarFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_calendar, container, false)

        return view
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