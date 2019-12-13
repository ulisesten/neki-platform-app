package com.neki.comedoresperanza.utils

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.neki.comedoresperanza.ByCategoryActivity
import com.neki.comedoresperanza.R
import kotlinx.android.synthetic.main.category_view.view.*

data class Category(val icon_name: String, val category_name: String)

class CategoryAdapter(context: Context, private val categories: MutableList<Category>): RecyclerView.Adapter<CategoryViewHolder>() {

    val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.category_view, parent, false)

        return CategoryViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, pos: Int) {
        val iconView: ImageView = holder.itemView.category_icon
        holder.itemView.category_name.text = categories[pos].category_name

        //val imgView = holder.itemView.imageView

        //Glide.with(mContext).load(categories[pos].icon_name).into(iconView)
        Glide.with(mContext).load( getDrawable(categories[pos].category_name) ).into(iconView)

        holder.itemView.setOnClickListener {

            //Toast.makeText(mContext, "Por implementar categoria", Toast.LENGTH_SHORT).show()
            val intent = Intent(mContext, ByCategoryActivity::class.java)
            intent.putExtra("category", categories[pos].category_name )
            mContext.startActivity(intent)

        }
    }
}

class CategoryViewHolder(v: View): RecyclerView.ViewHolder(v)

fun getDrawable(name: String): Int {

    var draw: Int

    when (name) {

        "música" -> draw = R.drawable.category_music_black_24dp

        "computación" -> draw = R.drawable.category_computer_black_24dp

        "deportes" -> draw = R.drawable.category_deportes_black_24dp

        "videojuegos" -> draw = R.drawable.category_juegos_black_24dp

        "mascotas" -> draw = R.drawable.category_mascotas_black_24dp

        "ofertas" -> draw = R.drawable.category_offer_black_24dp

        "más" -> draw = R.drawable.category_mas_black_24dp

        "libros" -> draw = R.drawable.category_library_black_24dp

        else -> draw = 0

    }

    return draw
}

