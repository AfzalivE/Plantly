package com.spacebitlabs.plantly.plantdetail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Photo
import com.spacebitlabs.plantly.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.add_photo_list_item.view.*
import kotlinx.android.synthetic.main.photo_list_item.view.*

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

    sealed class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(photo: Photo) = Unit

        class PhotoItemHolder(itemView: View) : PhotoHolder(itemView) {
            private val photoView = itemView.photo

            override fun bind(photo: Photo) {
                val filePath = if (photo.simplePhoto.filePath.startsWith("content://")) {
                    photo.simplePhoto.filePath
                } else {
                    "file://${photo.simplePhoto.filePath}"
                }
                Picasso.get()
                    .load(filePath)
                    .into(photoView)
            }
        }

        class AddPhotoHolder(itemView: View, private val addPhotoClickListener: () -> Unit) : PhotoHolder(itemView) {
            private val addPhotosButton = itemView.add_photo_button

            init {
                addPhotosButton.setOnClickListener {
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
