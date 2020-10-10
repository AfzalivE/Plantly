package com.spacebitlabs.plantly.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Photo
import com.spacebitlabs.plantly.plantdetail.PhotosAdapter
import com.spacebitlabs.plantly.plantdetail.PlantDetailFragment
import com.spacebitlabs.plantly.toBundle
import kotlinx.android.synthetic.main.fragment_photos.*

class PhotosFragment : DialogFragment() {

    private lateinit var viewModel: PhotosViewModel
    private var photoId: Long = 0
    private val photosAdapter = PhotosAdapter(showInPages = true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_photos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PhotosViewModel::class.java]

        photoId = arguments?.let {
            PhotosFragmentArgs.fromBundle(it).photoId
        } ?: 0

        viewModel.photoViewState.observe(viewLifecycleOwner, Observer { state ->
            render(state)
        })

        photo_pager.adapter = photosAdapter
        bottom_bar.inflateMenu(R.menu.photo_menu)
        bottom_bar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.make_cover -> makeCover()
                R.id.delete     -> deletePhoto()
                else            -> Unit
            }

            return@setOnMenuItemClickListener true
        }

        viewModel.getPhotos(photoId)
    }

    private fun makeCover() {
        viewModel.useAsCoverPhoto(photoId)
        notifyPhotoChangedState()
    }

    private fun deletePhoto() {
        viewModel.deletePhoto(photoId)
        notifyPhotoChangedState()
    }

    /**
     * Apparently this is the recommended way of
     * setting a result on the previous fragment
     * in the backstack. Like a setResult for
     * Jetpack Navigation.
     */
    private fun notifyPhotoChangedState() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(PlantDetailFragment.PHOTOS_CHANGED_STATE, true)
    }

    private fun render(state: PhotoViewState?) {
        when (state) {
            is PhotoViewState.PhotosLoaded -> renderPhoto(state.photos)
            else                           -> Unit
        }
    }

    private fun renderPhoto(photos: List<Photo>) {
        photosAdapter.setPhotoList(photos)
        photo_pager.setCurrentItem(photos.indexOfFirst { it.photoId == photoId }, false)
    }

    companion object {
        private const val PHOTO_ID: String = "photo_id"

        fun show(view: View, photo: Photo) {
            Navigation.findNavController(view).navigate(R.id.to_photo_action, photo.photoId.toBundle(PHOTO_ID))
        }
    }
}
