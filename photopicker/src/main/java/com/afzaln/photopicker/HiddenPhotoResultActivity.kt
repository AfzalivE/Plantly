package com.afzaln.photopicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

class HiddenPhotoResultActivity : Activity() {

    companion object {
        var resultCallback: ((Int, String) -> Unit)? = null

        fun showPhotos(context: Context) {
            val intent = Intent(context, HiddenPhotoResultActivity::class.java)
            context.startActivity(intent)
        }

        const val REQUEST_SHOW_PHOTOS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPhotosActivity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SHOW_PHOTOS) {
            if (data != null) {
                resultCallback?.invoke(resultCode, data.data.toString())
            }
        }

        finish()
    }

    private fun startPhotosActivity() {
        val showPhotosIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
//            addCategory(Intent.CATEGORY_APP_GALLERY)
            type = "image/*"
        }

        startActivityForResult(showPhotosIntent, REQUEST_SHOW_PHOTOS)
    }
}