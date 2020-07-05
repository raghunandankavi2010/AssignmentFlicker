package com.example.flcikersample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flcikersample.GlideApp
import com.example.flcikersample.R
import com.example.flcikersample.data.models.Photo

class FlickerImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.title)
    private val imageView: ImageView = view.findViewById(R.id.imageView)

    companion object {
        fun create(parent: ViewGroup): FlickerImageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item, parent, false)
            return FlickerImageViewHolder(view)
        }
    }

    fun bind(photo: Photo?) {
        title.text = photo?.title
        photo?.let {
            title.text = photo.title
            val url = "https://farm".plus(photo.farm).plus(".staticflickr.com/")
                .plus(photo.server)
                .plus("/")
                .plus(photo.id).plus("_")
                .plus(photo.secret)
                .plus("_m.jpg")

            GlideApp.with(imageView.context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .fitCenter()
                .into(imageView)
        }
    }

}