package com.afzaln.photopicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import com.afzaln.photopicker.OperationPickerDialogFragment.Companion.DEFAULT_TITLE
import com.afzaln.photopicker.OperationPickerDialogFragment.Companion.SHOW_PHOTOS
import com.afzaln.photopicker.OperationPickerDialogFragment.Companion.TAKE_PHOTO
import java.io.File

class PhotoPicker(
    val context: Context,
    private val imageView: ImageView?,
    private val addToGallery: Boolean,
    private inline val onSave: (String) -> Unit,
    private val authorities: String
) {

    fun takePicture() {
        val file = context.createOutputFile()
        val photoUri = FileProvider.getUriForFile(context, authorities, file)
        HiddenCameraResultActivity.resultCallback = { resultCode ->
            when (resultCode) {
                Activity.RESULT_OK -> {
                    imageView?.setScaledImageBitmap(file.absolutePath)
                    onSave.invoke(file.absolutePath)
                    if (addToGallery) {
                        addPhotoToGallery(file.absolutePath)
                    }
                }
                else -> {
                    file.delete()
                }
            }
            // Potential for memory leak
            HiddenCameraResultActivity.resultCallback = null
        }

        HiddenCameraResultActivity.takePicture(context, file.absolutePath, photoUri)
    }

    fun showPhotos() {
        HiddenPhotoResultActivity.resultCallback = { resultCode: Int, fileUri: String ->
            when (resultCode) {
                Activity.RESULT_OK -> {
                    imageView?.setScaledImageBitmap(fileUri)
                    onSave.invoke(fileUri)
                }
            }

            // Potential for memory leak
            HiddenPhotoResultActivity.resultCallback = null
        }

        HiddenPhotoResultActivity.showPhotos(context)
    }

    private fun addPhotoToGallery(currentPhotoPath: String) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(currentPhotoPath)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
    }

    private fun showDialog(fragmentManager: FragmentManager, @StringRes title: Int = DEFAULT_TITLE) {
        OperationPickerDialogFragment()
            .setTitle(title)
            .addListener { option ->
                when (option) {
                    SHOW_PHOTOS -> showPhotos()
                    TAKE_PHOTO  -> takePicture()
                }
            }
            .show(fragmentManager, OPERATION_PICKER_FRAGMENT_TAG)
    }

    class Builder(val context: Context) {
        private val authorities = "${context.packageName}.fileprovider"
        private var imageView: ImageView? = null
        private var onSave: (String) -> Unit = {}
        private var addToGallery: Boolean = false

        fun intoImageView(imageView: ImageView): Builder {
            this.imageView = imageView
            return this
        }

        fun addToGallery(): Builder {
            addToGallery = true
            return this
        }

        /**
         * Launch a camera intent to take a photo
         */
        fun takePicture() {
            build().takePicture()
        }

        /**
         * Show a screen to select photos from
         */
        fun showPhotos() {
            build().showPhotos()
        }

        /**
         * Show a dialog that allows the user to
         * choose whether to select a photo
         * or take a new one
         */
        fun showDialog(fragmentManager: FragmentManager, @StringRes title: Int = DEFAULT_TITLE) {
            build().showDialog(fragmentManager, title)
        }


        private fun build(): PhotoPicker {
            return PhotoPicker(context, imageView, addToGallery, onSave, authorities)
        }

        fun onSave(onSave: (String) -> Unit): Builder {
            this.onSave = onSave
            return this
        }
    }

    companion object {

        const val OPERATION_PICKER_FRAGMENT_TAG = "operationPickerTag"

        fun with(context: Context): Builder = Builder(context)
    }
}
