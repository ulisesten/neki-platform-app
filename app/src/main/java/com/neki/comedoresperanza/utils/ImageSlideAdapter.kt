package com.neki.comedoresperanza.utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.neki.comedoresperanza.R
import kotlinx.android.synthetic.main.image_view_holder.view.*

data class Image(val imageId: String)
class ImageViewHolder(v: View): RecyclerView.ViewHolder(v)

class ImageSlideAdapter(context: Context, private val images: MutableList<Image>): RecyclerView.Adapter<ImageViewHolder>() {

    val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.image_view_holder, parent, false)

        return ImageViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, pos: Int) {
        val imageView: ImageView = holder.itemView.image_slider_holder
        //holder.itemView.category_name.text = images[pos].imageId

        Glide.with(mContext).load( "https://res.cloudinary.com/djlzeapiz/image/upload/v1573315852/" + images[pos].imageId ).into(imageView)

    }
}
