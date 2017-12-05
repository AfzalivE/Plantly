package com.spacebitlabs.plantly.addplant

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.models.Plant
import kotlinx.android.synthetic.main.activity_addplant.*

/**
 * Allows user to lookup and add a plant
 */
class AddPlantActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplant)

        val model = ViewModelProviders.of(this).get(AddPlantViewModel::class.java)
        model.addPlantViewState.observe(this, Observer { state ->
            // TODO show suggestions in a dropdown list
            render(state)
        })

        save.setOnClickListener {
            // TODO Validate input
            // construct a plant from input
            val plant = Plant("Sansa")
            model.addPlant(plant)
        }
    }

    private fun render(state: AddPlantViewState?) {

    }

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddPlantActivity::class.java))
        }
    }
}