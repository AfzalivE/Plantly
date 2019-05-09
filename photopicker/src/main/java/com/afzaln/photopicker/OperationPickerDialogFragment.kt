package com.afzaln.photopicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OperationPickerDialogFragment : BottomSheetDialogFragment() {

    private var title: Int = R.string.add_photo
    private lateinit var listener: (Int) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_operation_picker, container, false)
        view.findViewById<TextView>(R.id.title).setText(title)
        
        view.findViewById<TextView>(R.id.show_photos).setOnClickListener {
            listener.invoke(SHOW_PHOTOS)
            dismiss()
        }

        view.findViewById<TextView>(R.id.take_photo).setOnClickListener {
            listener.invoke(TAKE_PHOTO)
            dismiss()
        }
        return view
    }

    fun addListener(listener: (Int) -> Unit): OperationPickerDialogFragment {
        this.listener = listener
        return this
    }

    fun setTitle(@StringRes title: Int = DEFAULT_TITLE): OperationPickerDialogFragment {
        this.title = title
        return this
    }

    companion object {
        const val SHOW_PHOTOS = 1
        const val TAKE_PHOTO = 2
        val DEFAULT_TITLE = R.string.add_photo
    }
}