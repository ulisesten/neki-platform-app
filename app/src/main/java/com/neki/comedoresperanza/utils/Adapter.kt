package com.neki.comedoresperanza.utils

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.neki.comedoresperanza.ProductDetailsActivity
import com.neki.comedoresperanza.R
import kotlinx.android.synthetic.main.list_row.view.*

data class Products(val userId:String, val userName:String, val id:String, val name: String, val description: String, val price: String, val imageUrl: String){}

class MainAdapter(context: Context, private val elementos: MutableList<Products>): RecyclerView.Adapter<MyViewHolder>() {

    private val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_row, parent, false)

        return MyViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return elementos.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.itemView.product_name.text = elementos[pos].name
        holder.itemView.product_description.text = elementos[pos].description
        holder.itemView.product_price.text = elementos[pos].price
        val imgView = holder.itemView.imageView

        Glide.with(mContext).load(elementos[pos].imageUrl).into(imgView)

        holder.itemView.setOnClickListener {

            //Toast.makeText(mContext, "Por implementar", Toast.LENGTH_SHORT).show()
            val intent = Intent(mContext, ProductDetailsActivity::class.java)
            intent.putExtra ("userId", elementos[pos].userId)
            intent.putExtra ( "userName", elementos[pos].userName)
            intent.putExtra("productId", elementos[pos].id)
            mContext.startActivity(intent)

        }


    }


}

class MyViewHolder(v: View): RecyclerView.ViewHolder(v) {

}
