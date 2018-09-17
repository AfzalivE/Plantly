package com.spacebitlabs.plantly.addplant

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afzaln.photopicker.PhotoPicker
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.hideKeyboard
import com.spacebitlabs.plantly.wordsFreqInMillis
import kotlinx.android.synthetic.main.fragment_addplant.*


/**
 * Allows user to lookup and add a plant
 */
class AddPlantFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_addplant, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProviders.of(this).get(AddPlantViewModel::class.java)
        model.addPlantViewState.observe(this, Observer { state ->
            state?.let { render(it) }
        })

        renderEmpty()

        save.setOnClickListener {
            activity?.hideKeyboard()
            var isValid = true

            if (type.text.isNullOrEmpty()) {
                type.error = "Type cannot be empty"
                isValid = false
            }

            if (name.text.isNullOrEmpty()) {
                name.error = "Name cannot be empty"
                isValid = false
            }

            if (water_freq.text.isNullOrEmpty()) {
                water_freq.error = "Water frequency is required"
                isValid = false
            }

            if (soil_freq.text.isNullOrEmpty()) {
                soil_freq.error = "Soil frequency is required"
                isValid = false
            }

            if (!isValid) return@setOnClickListener

            val waterFreq = water_freq.text.toString().wordsFreqInMillis(water_freq_type.selectedItem.toString())
            val soilFreq = soil_freq.text.toString().wordsFreqInMillis(soil_freq_type.selectedItem.toString())

            // construct a plant from input
            val plant = Plant(type.text.toString(), name.text.toString(), waterFreq, soilFreq)
            model.savePlant(plant)
        }

        cover_photo.setOnClickListener {
            context ?: return@setOnClickListener
            activity?.hideKeyboard()
            PhotoPicker
                .with(context!!)
                .intoImageView(cover_photo)
                .onSave { filePath: String ->
                    // TODO clean this somehow?
                    model.addPlantCoverImage(filePath)
                }
                .takePicture()
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
        Toast.makeText(activity, R.string.plant_saved, Toast.LENGTH_SHORT).show()
        // TODO nav close fragment
        findNavController().navigateUp()
    }

    private fun renderEmpty() {
        // TODO set up nav icon
    }

    private fun renderLoadingSuggestions() {

    }

    private fun renderPlantSelected(state: AddPlantViewState.PlantSelected) {
        // show plant data
//        name.setValue(state.plant.name)
    }


    private fun renderSuggestions(state: AddPlantViewState.SuggestionsFound) {

    }

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddPlantFragment::class.java))
        }
    }
}