package com.spacebitlabs.plantly.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.plants.PlantsViewModel

/**
 * Shows a calendar of past and upcoming watering dates.
 *
 * Green for a completed job
 * Red for a missed job
 * Yellow for make-up job
 * Blue for an upcoming job
 */
class CalendarFragment : Fragment() {

    private lateinit var model: PlantsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_calendar, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(activity!!)[PlantsViewModel::class.java]
    }

    companion object {
        fun newInstance(): CalendarFragment {
            val args = Bundle().apply { /* put params */ }
            val fragment = CalendarFragment()
            fragment.arguments = args
            return fragment
        }
    }
}