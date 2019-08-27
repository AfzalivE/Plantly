package com.afzaln.photopicker

import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

fun ImageView.setScaledImageBitmap(photoPath: String) {
    // Get the dimensions of the View
    val targetW = width
    val targetH = height

    // Get the dimensions of the bitmap
    val bmOptions = BitmapFactory.Options()
    bmOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(photoPath, bmOptions)
    val photoW = bmOptions.outWidth
    val photoH = bmOptions.outHeight

    // Determine how much to scale down the image
    val scaleFactor = min(photoW / targetW, photoH / targetH)

    // Decode the image file into a Bitmap sized to fill the View
    bmOptions.inJustDecodeBounds = false
    bmOptions.inSampleSize = scaleFactor

    val bitmap = BitmapFactory.decodeFile(photoPath, bmOptions)
    setImageBitmap(bitmap)
}

fun saveImage(fileUri: Uri, contentResolver: ContentResolver, outputFile: File): String {
    val inputStream = contentResolver.openInputStream(fileUri)

    // copy inputStream to file
    val outputStream = FileOutputStream(outputFile)
    inputStream.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    return outputFile.absolutePath
}

fun Context.createOutputFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    )

    return file
}