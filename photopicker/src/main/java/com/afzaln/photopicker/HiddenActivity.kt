package com.afzaln.photopicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

class HiddenActivity : Activity() {

    private lateinit var photoPath: String
    private lateinit var photoUri: Uri

    companion object {
        private const val EXTRA_URI = "uri"
        private const val EXTRA_PATH = "path"

        var resultCallback: ((Int) -> Unit)? = null

        fun takePicture(context: Context, photoPath: String, photoUri: Uri) {
            val intent = Intent(context, HiddenActivity::class.java).apply {
                    putExtra(EXTRA_URI, photoUri)
                    putExtra(EXTRA_PATH, photoPath)
                }
            context.startActivity(intent)
        }

        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            photoPath = intent.getStringExtra(EXTRA_PATH)
            photoUri = intent.getParcelableExtra(EXTRA_URI)
        }

        takePicture()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            resultCallback?.invoke(resultCode)
        }

        finish()
    }

    fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager ?: return) != null) {
            // Continue only if the File was successfully created
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
}