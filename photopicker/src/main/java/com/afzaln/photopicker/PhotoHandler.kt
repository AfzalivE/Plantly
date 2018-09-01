package com.afzaln.photopicker

import android.graphics.BitmapFactory
import android.widget.ImageView


object PhotoHandler {
    fun setPic(imageView: ImageView?, photoPath: String) {
        imageView ?: return

        // Get the dimensions of the View
        val targetW = imageView.width
        val targetH = imageView.height

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(photoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(photoPath, bmOptions)
        imageView.setImageBitmap(bitmap)
    }
}
