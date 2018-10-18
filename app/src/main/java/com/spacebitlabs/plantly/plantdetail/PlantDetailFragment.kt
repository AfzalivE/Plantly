package com.spacebitlabs.plantly.plantdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
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

class PlantDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_plant_detail, container, false)

    private val viewModel: PlantDetailViewModel
        get() {
            return ViewModelProviders.of(this).get(PlantDetailViewModel::class.java)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantId = arguments?.let {
            PlantDetailFragmentArgs.fromBundle(it).plantId
        } ?: 0

        viewModel.plantDetailViewState.observe(this, Observer { state ->
            render(state)
        })

        viewModel.getPlantDetail(plantId)
    }

    private fun render(state: PlantDetailViewState?) {
        when (state) {
            is PlantDetailViewState.PlantDetailLoaded -> renderPlantDetail(state.plant, state.birthday, state.waterCount, state.soilCount)
        }
    }

    private fun renderPlantDetail(plantWithPhotos: PlantWithPhotos, birthday: OffsetDateTime, waterCount: Int, soilCount: Int) {
        Timber.d("Rendering plantWithPhotos detail")
        plantWithPhotos.plant.let {
            if (it.coverPhoto.filePath != "") {
                Picasso.get().isLoggingEnabled = true
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

        setupClicks()
    }

    private fun setupClicks() {
        water_img.setOnClickListener {
            viewModel.waterPlant()
        }
    }

    companion object {
        private const val PLANT_ID: String = "plant_id"

        fun show(view: View, plant: Plant) {
            Navigation.findNavController(view).navigate(R.id.to_plant_detail_action, plant.id.toBundle(PLANT_ID))
        }
    }
}