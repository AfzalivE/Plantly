package com.spacebitlabs.plantly.plantdetail

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacebitlabs.plantly.R

/**
 * A placeholder fragment containing a simple view.
 */
class PlantDetailActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plant_detail, container, false)
    }
}
