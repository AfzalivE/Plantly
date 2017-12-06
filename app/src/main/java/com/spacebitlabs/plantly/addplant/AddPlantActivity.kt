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
            val plant = Plant(4, "Sansa2")
            model.savePlant(plant)
        }
    }

    private fun render(state: AddPlantViewState?) {
        when (state) {
            is AddPlantViewState.Loading -> renderLoadingSuggestions()
            is AddPlantViewState.Empty -> renderNoSuggestionsFound()
            is AddPlantViewState.SuggestionsFound -> renderSuggestions(state)
            is AddPlantViewState.PlantSelected -> renderPlantSelected(state)
        }
    }

    private fun renderNoSuggestionsFound() {

    }

    private fun renderLoadingSuggestions() {

    }

    private fun renderPlantSelected(state: AddPlantViewState.PlantSelected) {
        // show plant data
        add_plant_text.setText(state.plant.name)
    }


    private fun renderSuggestions(state: AddPlantViewState.SuggestionsFound) {

    }

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddPlantActivity::class.java))
        }
    }
}