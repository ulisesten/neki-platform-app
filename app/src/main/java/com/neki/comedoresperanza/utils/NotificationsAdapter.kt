package com.neki.comedoresperanza.utils

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.neki.comedoresperanza.AnswerToMessagesActivity
import com.neki.comedoresperanza.ProductDetailsActivity
import com.neki.comedoresperanza.R
import kotlinx.android.synthetic.main.notification_row.view.*

data class Notif(val image:String, val title:String, val content:String)

class NotificationsAdapter(context: Context, private val elementos: MutableList<Message>): RecyclerView.Adapter<MyViewHolder>() {
    private val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.notification_row, parent, false)

        return MyViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return elementos.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

            holder.itemView.notification_title.text = elementos[pos].senderName
            holder.itemView.notification_content.text = elementos[pos].message
            val imgView = holder.itemView.notification_image

            Glide.with(mContext).load("https://res.cloudinary.com/djlzeapiz/image/upload/v1573315852/user_profile" /**+ elementos[pos].fromId*/).into(imgView)

            holder.itemView.setOnClickListener {

                //Toast.makeText(mContext, "Por implementar", Toast.LENGTH_SHORT).show()
                val intent = Intent(mContext, AnswerToMessagesActivity::class.java)
                intent.putExtra("fromId", elementos[pos].fromId)
                intent.putExtra("toId", elementos[pos].toId)
                intent.putExtra("senderName", elementos[pos].senderName)
                intent.putExtra("receiverName", elementos[pos].receiverName)
                intent.putExtra("productId", elementos[pos].productId)

                mContext.startActivity(intent)

            }

    }

}