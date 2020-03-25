package com.spacebitlabs.plantly.photo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.data.entities.Photo
import kotlinx.coroutines.launch

class PhotosViewModel : ViewModel() {

    val photoViewState: MutableLiveData<PhotoViewState> = MutableLiveData()

    private val userPlantsStore by lazy {
        Injection.get().providePlantStore()
    }

    fun getPhotos(photoId: Long) {
        viewModelScope.launch {
            val photo = userPlantsStore.getPhoto(photoId)
            val photos = userPlantsStore.getPhotosOfPlant(photo.plantId)
            photoViewState.value = PhotoViewState.PhotosLoaded(photos)
        }
    }

    fun useAsCoverPhoto(photoId: Long) {
        viewModelScope.launch {
            userPlantsStore.setAsCoverPhoto(photoId)
        }
    }

    fun deletePhoto(photoId: Long) {
        viewModelScope.launch {
            userPlantsStore.deletePhoto(photoId)
        }
    }
}

sealed class PhotoViewState {
    object Error : PhotoViewState()
    class PhotosLoaded(val photos: List<Photo>) : PhotoViewState()
}
