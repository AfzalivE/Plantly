package com.spacebitlabs.plantly.plantdetail

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
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
import org.threeten.bp.Period
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.extra.AmountFormats
import timber.log.Timber
import java.util.*

class PlantDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_plant_detail, container, false)

    private lateinit var viewModel: PlantDetailViewModel

    private val addPhotoClickListener: () -> Unit = {
        context?.let {
            PhotoPicker.with(context!!)
                .addToGallery()
                .onSave { filePath: String ->
                    Timber.d("Saved photo at $filePath")
                    viewModel.addPlantPhoto(filePath)
                }
                .showPhotos()
        }
    }

    private val photosAdapter = PhotosAdapter(addPhotoClickListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PlantDetailViewModel::class.java)

        val plantId = arguments?.let {
            PlantDetailFragmentArgs.fromBundle(it).plantId
        } ?: 0

        viewModel.plantDetailViewState.observe(this, Observer { state ->
            render(state)
        })

        photos_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        photos_list.adapter = photosAdapter

        viewModel.getPlantDetail(plantId)
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
        plantWithPhotos.plant.let { plant ->
            if (plant.coverPhoto.filePath != "") {
                Picasso.get()
                    .load("file://${plant.coverPhoto.filePath}")
                    .fit()
                    .centerCrop()
                    .into(cover_photo)
            }

            title.text = plant.name
//            type.text = it.type
            val waterFreqDuration = Period.of(0, 0, plant.waterFreq.millisFreqToDays())
            water_freq.text = AmountFormats.wordBased(waterFreqDuration, Locale.getDefault())
            val soilFreqDuration = Period.of(0, 0, plant.soilFreq.millisFreqToDays())
            soil_freq.text = AmountFormats.wordBased(soilFreqDuration, Locale.getDefault())
        }

        plantWithPhotos.photos.let {
            photos_count.text = resources.getQuantityString(R.plurals.photos, it.size, it.size)
            photosAdapter.setPhotoList(it)
        }

        birthday_txt.text = LocalDate.from(birthday).format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy"))
        water_count.text = resources.getQuantityString(R.plurals.watered, waterCount, waterCount)
        fertilize_count.text = resources.getQuantityString(R.plurals.fertilized, soilCount, soilCount)
        // TODO change from "today/yesterday/1 hour ago" to "1 day or 1 hour"

        val ageDuration = Period.between(birthday.toLocalDate(), LocalDate.now())
        age.text = AmountFormats.wordBased(ageDuration, Locale.getDefault())

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