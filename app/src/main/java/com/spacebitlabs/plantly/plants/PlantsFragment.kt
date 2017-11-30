package com.spacebitlabs.plantly.plants

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacebitlabs.plantly.R

/**
 * Created by afzal on 2017-11-29.
 */
class PlantsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_plants, container, false)

        return view
    }

    companion object {
        fun newInstance(): PlantsFragment {
            val args = Bundle().apply { /* put params */}
            val fragment = PlantsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}