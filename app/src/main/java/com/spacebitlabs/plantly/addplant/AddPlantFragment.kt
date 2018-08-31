package com.spacebitlabs.plantly.addplant

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.hideKeyboard
import com.spacebitlabs.plantly.wordsFreqInMillis
import kotlinx.android.synthetic.main.fragment_addplant.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*




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
            model.addPlant(plant)
        }

        cover_photo.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
    }

    private var currentPhotoPath: String? = null

    private fun dispatchTakePictureIntent() {
        activity ?: return

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity?.packageManager ?: return) != null) {
            // Create the File where the photo should go
            val photoFile = createImageFile()
            currentPhotoPath = photoFile.absolutePath
            val photoUri = FileProvider.getUriForFile(context!!, "com.spacebitlabs.plantly.fileprovider", photoFile)
            // Continue only if the File was successfully created
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun setPic() {
        currentPhotoPath ?: return

        // Get the dimensions of the View
        val targetW = cover_photo.width
        val targetH = cover_photo.height

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
        cover_photo.setImageBitmap(bitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data!!.extras
            setPic()
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

        private const val REQUEST_IMAGE_CAPTURE = 1
    }
}