package com.opentry.android_storage_samples.fileprovider.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.opentry.android_storage_samples.R
import java.io.File

class GalleryAdapter(private val onClick: (File) -> Unit) :
    ListAdapter<File, ImageViewHolder>(
        ListItemCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.gallery_item_layout, parent, false)
        return ImageViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val file = getItem(position)
        holder.rootView.tag = file

        holder.imageView.load(file) {
            crossfade(true)
        }
    }
}

class ListItemCallback : DiffUtil.ItemCallback<File>() {
    override fun areItemsTheSame(oldItem: File, newItem: File) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: File, newItem: File) =
        oldItem == newItem
}

/**
 * Basic [RecyclerView.ViewHolder] for our gallery.
 */
class ImageViewHolder(view: View, onClick: (File) -> Unit) : RecyclerView.ViewHolder(view) {
    val rootView = view
    val imageView: ImageView = view.findViewById(R.id.image)

    init {
        imageView.setOnClickListener {
            val image = rootView.tag as? File ?: return@setOnClickListener
            onClick(image)
        }
    }
}