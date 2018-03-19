package com.spacebitlabs.plantly.addplant

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import kotlinx.android.synthetic.main.activity_addplant.*

/**
 * Allows user to lookup and add a plant
 */
class AddPlantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplant)

        val model = ViewModelProviders.of(this).get(AddPlantViewModel::class.java)
        model.addPlantViewState.observe(this, Observer { state ->
            state?.let { render(it) }
        })

        renderEmpty()

        save.setOnClickListener {
            // TODO Validate input
            // construct a plant from input
            val plant = Plant(4, "Sansa2")
            model.addPlant(plant)
        }
    }

    private fun render(state: AddPlantViewState) {
        return when (state) {
            is AddPlantViewState.Loading          -> renderLoadingSuggestions()
            is AddPlantViewState.Empty            -> renderEmpty()
            is AddPlantViewState.SuggestionsFound -> renderSuggestions(state)
            is AddPlantViewState.PlantSelected    -> renderPlantSelected(state)
            is AddPlantViewState.Saved            -> renderPlantSaved()
        }
    }

    private fun renderPlantSaved() {
        Toast.makeText(this, R.string.plant_saved, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun renderEmpty() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
    }

    private fun renderLoadingSuggestions() {

    }

    private fun renderPlantSelected(state: AddPlantViewState.PlantSelected) {
        // show plant data
        name.setValue(state.plant.name)
    }


    private fun renderSuggestions(state: AddPlantViewState.SuggestionsFound) {

    }

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddPlantActivity::class.java))
        }
    }
}