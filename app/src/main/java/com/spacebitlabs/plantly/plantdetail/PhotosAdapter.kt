package com.spacebitlabs.plantly.plantdetail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Photo
import com.spacebitlabs.plantly.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.add_photo_list_item.*
import kotlinx.android.synthetic.main.photo_list_item.*

class PhotosAdapter(private val addPhotoClickListener: () -> Unit) : RecyclerView.Adapter<PhotosAdapter.PhotoHolder>() {
    private val photoList = arrayListOf<Photo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return when (viewType) {
            VIEW_PHOTO     -> PhotoHolder.PhotoItemHolder(parent.inflate(R.layout.photo_list_item))
            VIEW_ADD_PHOTO -> PhotoHolder.AddPhotoHolder(parent.inflate(R.layout.add_photo_list_item), addPhotoClickListener)
            else           -> PhotoHolder.PhotoItemHolder(parent.inflate(R.layout.photo_list_item))
        }
    }

    override fun getItemCount(): Int = photoList.size + 1 // add one for button

    override fun getItemViewType(position: Int): Int =
        if (position == photoList.size) {
            VIEW_ADD_PHOTO
        } else {
            VIEW_PHOTO
        }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        if (position < photoList.size) {
            holder.bind(photoList[position])
        }
    }

    fun setPhotoList(photos: List<Photo>) {
        photoList.clear()
        photoList.addAll(photos)
        notifyDataSetChanged()
    }

    sealed class PhotoHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        open fun bind(photo: Photo) = Unit

        class PhotoItemHolder(containerView: View) : PhotoHolder(containerView) {

            override fun bind(photo: Photo) {
                val filePath = if (photo.simplePhoto.filePath.startsWith("content://")) {
                    photo.simplePhoto.filePath
                } else {
                    "file://${photo.simplePhoto.filePath}"
                }
                Picasso.get()
                    .load(filePath)
                    .into(photo_view)
            }
        }

        class AddPhotoHolder(override val containerView: View, private val addPhotoClickListener: () -> Unit) : PhotoHolder(containerView), LayoutContainer {

            init {
                add_photo_button.setOnClickListener {
                    addPhotoClickListener.invoke()
                }
            }
        }
    }

    companion object {
        const val VIEW_PHOTO = 44
        const val VIEW_ADD_PHOTO = 45
    }

}
