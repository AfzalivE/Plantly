package com.spacebitlabs.plantly.plantdetail

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.afzaln.photopicker.PhotoPicker
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.entities.PlantWithPhotos
import com.spacebitlabs.plantly.millisFreqToDays
import com.spacebitlabs.plantly.toBundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_plant_detail.*
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class PlantDetailFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_plant_detail, container, false)

    private lateinit var viewModel: PlantDetailViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PlantDetailViewModel::class.java)

        val plantId = arguments?.let {
            PlantDetailFragmentArgs.fromBundle(it).plantId
        } ?: 0

        viewModel.plantDetailViewState.observe(this, Observer { state ->
            render(state)
        })

        viewModel.getPlantDetail(plantId)

        photos_button.setOnClickListener {
            context ?: return@setOnClickListener

            PhotoPicker.with(context!!)
                .addToGallery()
                .onSave { filePath: String ->
                    Timber.d("Saved photo at $filePath")

                }
                .takePicture()
        }
    }

    private fun render(state: PlantDetailViewState?) {
        when (state) {
            is PlantDetailViewState.PlantDetailLoaded -> renderPlantDetail(
                state.plant,
                state.birthday,
                state.waterCount,
                state.soilCount,
                state.nextWatering
            )
        }
    }

    private fun renderPlantDetail(
        plantWithPhotos: PlantWithPhotos,
        birthday: OffsetDateTime,
        waterCount: Int,
        soilCount: Int,
        nextWatering: OffsetDateTime
    ) {
        Timber.d("Rendering plantWithPhotos detail")
        plantWithPhotos.plant.let {
            if (it.coverPhoto.filePath != "") {
                Picasso.get()
                    .load("file://${it.coverPhoto.filePath}")
                    .fit()
                    .centerCrop()
                    .into(cover_photo)
            }

            title.text = it.name
//            type.text = it.type
            val waterFreqDays = it.waterFreq.millisFreqToDays()
            val soilFreqDays = it.soilFreq.millisFreqToDays()
            water_freq.text = resources.getQuantityString(R.plurals.days, waterFreqDays, waterFreqDays)
            soil_freq.text = resources.getQuantityString(R.plurals.days, soilFreqDays, soilFreqDays)
        }

        plantWithPhotos.photos.let {
            photos_count.text = resources.getQuantityString(R.plurals.photos, it.size, it.size)
        }

        birthday_txt.text = LocalDate.from(birthday).format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy"))
        water_count.text = resources.getQuantityString(R.plurals.watered, waterCount, waterCount)
        fertilize_count.text = resources.getQuantityString(R.plurals.fertilized, soilCount, soilCount)
        // TODO change from "today/yesterday/1 hour ago" to "1 day or 1 hour"
        age.text = DateUtils.getRelativeTimeSpanString(birthday.toInstant().toEpochMilli())

        next_watering_date.text = DateUtils.getRelativeTimeSpanString(nextWatering.toInstant().toEpochMilli())

        setupClicks()
    }

    private fun setupClicks() {
        water_img.setOnClickListener {
            viewModel.waterPlant()
            Toast.makeText(context, "Watered", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PLANT_ID: String = "plant_id"

        fun show(view: View, plant: Plant) {
            Navigation.findNavController(view).navigate(R.id.to_plant_detail_action, plant.id.toBundle(PLANT_ID))
        }
    }
}