package com.spacebitlabs.plantly.plantdetail

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.afzaln.photopicker.PhotoPicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.data.entities.PlantWithPhotos
import com.spacebitlabs.plantly.millisFreqToDays
import com.spacebitlabs.plantly.plantdetail.PlantDetailViewState.PlantDeleted
import com.spacebitlabs.plantly.plantdetail.PlantDetailViewState.PlantDetailLoaded
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

    private var plantWithPhotos: PlantWithPhotos? = null
    private lateinit var viewModel: PlantDetailViewModel

    private val addPhotoClickListener: () -> Unit = {
        context?.let {
            PhotoPicker.with(it)
                .addToGallery()
                .onSave { filePath: String ->
                    Timber.d("Saved photo at $filePath")
                    viewModel.addPlantPhoto(filePath)
                }
                .showDialog(childFragmentManager)
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

        more_menu.setOnClickListener {
            showPopupMenu(it)
        }

        viewModel.getPlantDetail(plantId)
    }

    private fun showPopupMenu(view: View) {
        context ?: return

        val popup = PopupMenu(context!!, view)
        popup.inflate(R.menu.plant_detail_actions)
        popup.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_change_cover_photo -> {
                    showCoverFilePicker()
                }
                R.id.menu_delete -> {
                    val plantName = plantWithPhotos?.plant?.name ?: "plant"
                    MaterialAlertDialogBuilder(context!!)
                        .setTitle(getString(R.string.delete_plant_title, plantName))
                        .setMessage(getString(R.string.delete_plant_message, plantName))
                        .setPositiveButton(android.R.string.yes) { _, _ ->
                            viewModel.deletePlant()
                        }
                        .setNegativeButton(android.R.string.no) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
            true
        }
        popup.show()
    }

    private fun showCoverFilePicker() {
        context ?: return
        Toast.makeText(context, R.string.future_feature, Toast.LENGTH_SHORT).show()
    }

    private fun render(state: PlantDetailViewState?) {
        when (state) {
            is PlantDetailLoaded -> renderPlantDetail(
                state.plant,
                state.birthday,
                state.waterCount,
                state.soilCount,
                state.nextWatering
            )
            is PlantDeleted -> {
                view ?: return
                Navigation.findNavController(view!!).navigateUp()
            }
        }
    }

    private fun renderPlantDetail(
        plantWithPhotos: PlantWithPhotos,
        birthday: OffsetDateTime,
        waterCount: Int,
        soilCount: Int,
        nextWatering: OffsetDateTime
    ) {
        this.plantWithPhotos = plantWithPhotos
        Timber.d("Rendering plantWithPhotos detail")
        plantWithPhotos.plant.let {
            if (it.coverPhoto.filePath.isEmpty()) {
                cover_photo.setImageResource(R.drawable.sample_plant)
            } else {
                Picasso.get()
                    .load("file://${it.coverPhoto.filePath}")
                    .fit()
                    .centerCrop()
                    .into(cover_photo)
            }

            title.text = it.name
//            type.text = it.type
            val waterFreqDuration = Period.of(0, 0, it.waterFreq.millisFreqToDays())
            water_freq.text = AmountFormats.wordBased(waterFreqDuration, Locale.getDefault())
            val soilFreqDuration = Period.of(0, 0, it.soilFreq.millisFreqToDays())
            soil_freq.text = AmountFormats.wordBased(soilFreqDuration, Locale.getDefault())
        }

        plantWithPhotos.photos.let {
            photos_count.text = resources.getQuantityString(R.plurals.photos, it.size, it.size)
            photosAdapter.setPhotoList(it)
        }

        birthday_txt.text = LocalDate.from(birthday).format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy"))
        water_count.text = resources.getQuantityString(R.plurals.watered, waterCount, waterCount)
        fertilize_count.text = resources.getQuantityString(R.plurals.fertilized, soilCount, soilCount)

        val ageDuration = Period.between(birthday.toLocalDate(), LocalDate.now())
        age.text = AmountFormats.wordBased(ageDuration, Locale.getDefault())

        next_watering_date.text = DateUtils.getRelativeTimeSpanString(nextWatering.toInstant().toEpochMilli())

        setupClicks()
    }

    private fun setupClicks() {
        water_btn.setOnClickListener {
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