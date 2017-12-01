package com.spacebitlabs.plantly.plants

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacebitlabs.plantly.MainViewModel
import com.spacebitlabs.plantly.R
import kotlinx.android.synthetic.main.fragment_plants.*

/**
 * Created by afzal on 2017-11-29.
 */
class PlantsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_plants, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProviders.of(activity).get(MainViewModel::class.java)
        model.getPlants().observe(this, Observer { item ->
            plants.text = item
        })
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