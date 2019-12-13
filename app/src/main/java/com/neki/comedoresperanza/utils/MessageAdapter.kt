package com.neki.comedoresperanza.utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.neki.comedoresperanza.R
import kotlinx.android.synthetic.main.incoming_message.view.*

data class Message(val fromId: String,
                   val toId: String,
                   val senderName: String,
                   val receiverName:String,
                   val message: String,
                   val productId:String,
                   val time:String )

class MessageAdapter(private val userId:String, private val elementos: MutableList<Message>): RecyclerView.Adapter<MyViewHolder>() {

    private val viewTypeMessageSent = 1
    private val viewTypeMessageReceived = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val layoutInflater: LayoutInflater
        var cellForRow: View? = null

        if( viewType == viewTypeMessageSent ) {
            layoutInflater = LayoutInflater.from(parent.context)
            cellForRow =
                layoutInflater.inflate(R.layout.outgoing_message, parent, false)
        }
        else if ( viewType == viewTypeMessageReceived ){
            layoutInflater = LayoutInflater.from(parent.context)
            cellForRow =
                layoutInflater.inflate(R.layout.incoming_message, parent, false)
        }
        return MyViewHolder(cellForRow!!)
    }

    override fun getItemCount(): Int {
        return elementos.size
    }

    override fun getItemViewType(position: Int): Int {

        val message = elementos[position]

        return if( message.fromId == userId ) viewTypeMessageSent
        else viewTypeMessageReceived

    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.itemView.text_message_body.text = elementos[pos].message
        holder.itemView.text_message_time.text = elementos[pos].time


        if( holder.itemViewType == viewTypeMessageReceived){

            holder.itemView.text_message_name.text = elementos[pos].senderName

            val imgView = holder.itemView.image_message_profile

            val mContext = holder.itemView.context

            Glide.with(mContext).load("https://res.cloudinary.com/djlzeapiz/image/upload/v1573315852/" + elementos[pos].fromId).into(imgView)

            holder.itemView.setOnClickListener {

                Toast.makeText(mContext, "To Implement", Toast.LENGTH_SHORT).show()

            }
        }

    }


}
