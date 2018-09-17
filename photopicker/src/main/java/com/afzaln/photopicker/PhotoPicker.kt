package com.afzaln.photopicker

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.support.v4.content.FileProvider
import android.widget.ImageView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoPicker(val context: Context, private val imageView: ImageView?, val file: File, private inline val onSave: (String) -> Unit) {

    fun takePicture() {
        val photoUri = FileProvider.getUriForFile(context, "com.spacebitlabs.photopicker.fileprovider", file)
        HiddenActivity.resultCallback = {
            when (it) {
                Activity.RESULT_OK -> {
                    PhotoHandler.setPic(imageView, file.absolutePath)
                    onSave.invoke(file.absolutePath)
                }
            }
            HiddenActivity.resultCallback = null
        }

        HiddenActivity.takePicture(context, file.absolutePath, photoUri)
    }

    fun showPhotos() {

    }

    fun showDialog() {

    }

    class Builder(val context: Context) {
        private var imageView: ImageView? = null
        private var file: File
        private var onSave: (String) -> Unit = {}

        init {
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            file = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
            )
        }

        fun intoImageView(imageView: ImageView): Builder {
            this.imageView = imageView
            return this
        }

        fun toFile(file: File): Builder {
            this.file = file
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
        fun showDialog() {
            build().showDialog()
        }


        private fun build(): PhotoPicker {
            return PhotoPicker(context, imageView, file, onSave)
        }

        fun onSave(onSave: (String) -> Unit): Builder {
            this.onSave = onSave
            return this
        }
    }

    companion object {
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }
}
